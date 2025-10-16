package com.example.ApiUser.service.movies.interactionService.listForYou;

import com.example.ApiUser.dto.request.movies.MovieFilterRequest;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.callMovies.Category;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.MovieDTO;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.movies.MovieDTOMapper;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.movies.MovieCallRepository;
import com.example.ApiUser.repository.movies.interationMovie.LikeRepository;
import com.example.ApiUser.repository.movies.interationMovie.WatchingRepository;
import com.example.ApiUser.service.helper.BuildSpecificationHelper;
import com.example.ApiUser.service.redis.RedisServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ListForYouService {
    UserRepository userRepository;
    MovieCallRepository movieRepository;
    WatchingRepository watchingRepository;
    LikeRepository likeRepository;
    MovieDTOMapper movieDTOMapper;
    RedisServiceImpl redisService;
    BuildSpecificationHelper buildSpecificationHelper;


    @Async
    public void refreshRecommendations(String userId) {
        try {
            log.info("Refreshing recommendations for user {}", userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            List<String> combinedIds = new ArrayList<>();
            combinedIds.addAll(
                    watchingRepository.findAllByUser(user).stream()
                            .map(w -> w.getMovie().getId())
                            .toList()
            );
            combinedIds.addAll(
                    likeRepository.findAllByUser(user).stream()
                            .map(like -> like.getMovie().getId())
                            .toList()
            );

            List<Movie> listMovie = combinedIds.isEmpty()
                    ? movieRepository.findTop50ByYearAndStatusOrderByCreatedDesc("2025", "ongoing")
                    : movieRepository.findAllById(combinedIds);

            String mostCommonType = listMovie.stream()
                    .collect(Collectors.groupingBy(Movie::getType, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            if (mostCommonType == null) {
                listMovie = movieRepository.findTop50ByYearAndStatusOrderByCreatedDesc("2025", "ongoing");
            }

            String mostCommonCategory = listMovie.stream()
                    .filter(m -> m.getType().equals(mostCommonType))
                    .flatMap(m -> m.getCategories().stream())
                    .collect(Collectors.groupingBy(Category::getSlug, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            List<String> recommendedMovie;
            if (mostCommonCategory != null) {
                PageRequest page = PageRequest.of(0, 200);
                recommendedMovie = movieRepository.findIdsByTypeAndCategorySlug(mostCommonType, mostCommonCategory, page);
            } else {
                recommendedMovie = movieRepository.findTop50IdsByYearAndStatus("2025", "ongoing");
                log.info("List khi null:{}", recommendedMovie.size());
            }

            String key = "recommendations:";
            redisService.hashSet(key, userId, recommendedMovie);
            redisService.setTimeToLive(key, 7);
            log.info("Recommendations cached for user {}", userId);
        } catch (Exception e) {
            log.error("Failed to refresh recommendations for user {}: {}", userId, e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated")
//    @SuppressWarnings("unchecked")
    public List<MovieDTO> getRecommendations(MovieFilterRequest filterRequest, String userId, Pageable pageable) {
        String key = "recommendations:";
        Object cached = redisService.hashGet(key, userId);

        Page<Movie> moviePage;

        if (cached != null) {
            List<String> cachedIds = new ArrayList<>();
            if (cached instanceof List<?>) {
                for (Object o : (List<?>) cached) {
                    if (o instanceof String s) {
                        cachedIds.add(s);
                    }
                }
            }

            Specification<Movie> spec = buildSpecificationHelper.buildSpecification(filterRequest)
                    .and((root, query, cb) -> root.get("id").in(cachedIds));

            moviePage = movieRepository.findAll(spec, pageable);

            if (moviePage.isEmpty()) {
                moviePage = getDefaultMoviesWithFilter(filterRequest, pageable);
            }
        } else {
            refreshRecommendations(userId);
            moviePage = getDefaultMoviesWithFilter(filterRequest, pageable);
        }

        return moviePage.getContent().stream()
                .map(movieDTOMapper::toDTO)
                .toList();
    }

    private Page<Movie> getDefaultMoviesWithFilter(MovieFilterRequest filterRequest, Pageable pageable) {
        Specification<Movie> spec = buildSpecificationHelper.buildSpecification(filterRequest)
                .and((root, query, cb) -> cb.equal(root.get("year"), "2025"))
                .and((root, query, cb) -> cb.equal(root.get("status"), "ongoing"));

        return movieRepository.findAll(spec, pageable);
    }
}



























