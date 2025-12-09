package com.example.ApiUser.service.admin.callMovies;

import com.example.ApiUser.dto.response.callMovies.MovieCallResponse;
import com.example.ApiUser.dto.response.callMovies.getSlug.MoviePageResponse;
import com.example.ApiUser.repository.movies.MovieCallRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class CallSlugService {
    String BASE_URL = "https://phimapi.com/danh-sach/phim-moi-cap-nhat?page=%d";
    String MOVIE_DETAIL_URL = "https://phimapi.com/phim/%s"; // chi tiết phim theo slug

    int MAX_RETRY = 5; // số lần thử lại khi bị lỗi

    @Autowired
    private CallMovieService movieService;

    @Autowired
    private MovieCallRepository movieRepository;

    public Set<String> crawlAllSlug() {

        RestTemplate restTemplate = new RestTemplate();
        // ít thôi để tránh bị block
        int THREADS = 12;
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        // Gọi page=1 để lấy totalPages
        MoviePageResponse firstPage = restTemplate.getForObject(String.format(BASE_URL, 1), MoviePageResponse.class);
        if (firstPage == null || firstPage.getPagination() == null) {
            System.out.println("Không lấy được dữ liệu!");
            return null;
        }

        int totalPages = firstPage.getPagination().getTotalPages();
//        int totalPages = 1;
        System.out.println("Tổng số trang: " + totalPages);

        // List để chứa slug
        Set<String> allSlugs = ConcurrentHashMap.newKeySet();
        firstPage.getItems().forEach(item -> allSlugs.add(item.getSlug()));

        // Crawl từ page 2 -> totalPages
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int page = 2; page <= totalPages; page++) {
            final int currentPage = page;
            futures.add(CompletableFuture.runAsync(() -> {
                int retry = 0;
                Random random = new Random();

                while (retry < MAX_RETRY) {
                    try {
                        MoviePageResponse response = restTemplate.getForObject(
                                String.format(BASE_URL, currentPage), MoviePageResponse.class);

                        if (response != null && response.getItems() != null) {
                            response.getItems().forEach(item ->
                                    allSlugs.add(item.getSlug())
                            );

                            Thread.sleep(500 + random.nextInt(1000));
                            return;
                        }
                    } catch (Exception e) {
                        retry++;
                        System.out.println("Lỗi ở page " + currentPage + " (thử lại " + retry + "): " + e.getMessage());
                        try {
                            Thread.sleep(3000L * retry); // exponential backoff
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
                System.out.println("Bỏ qua page " + currentPage + " sau " + MAX_RETRY + " lần thất bại!");
            }, executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();

        System.out.println("✅ Crawl xong dữ liệu phim!");
        System.out.println("📌 Tổng số slug đã lấy: " + allSlugs.size());
//        System.out.println("URL gọi: " + String.format(BASE_URL, 1));
//        System.out.println("Số item trong page 1: " + firstPage.getItems().size());
//        firstPage.getItems().forEach(i -> System.out.println(i.getSlug()));

        return allSlugs;
    }

    private record SlugSets(Set<String> newSlugs, Set<String> duplicates) {
    }

    private SlugSets checkSlug() {
        Set<String> slugs = crawlAllSlug();

        Set<String> existedSlugs = new HashSet<>(movieRepository.findAllSlugs());

        Set<String> duplicates = slugs.parallelStream()
                .filter(existedSlugs::contains)
                .collect(Collectors.toSet());

        Set<String> newSlugs = slugs.parallelStream()
                .filter(slug -> !existedSlugs.contains(slug))
                .collect(Collectors.toSet());
        return new SlugSets(newSlugs, duplicates);
    }

    @Async
//    @PreAuthorize("hasRole('ADMIN')")
    public void createCallData() {
        SlugSets sets = checkSlug();
        Set<String> newSlugs = sets.newSlugs();

        if (newSlugs.isEmpty()) {
            System.out.println("✅ Không có slug mới để import!");
            return;
        }

        newSlugs.forEach(slug -> {
            String apiUrl = String.format(MOVIE_DETAIL_URL, slug);
            try {
                MovieCallResponse movie = movieService.newData(apiUrl);
                System.out.println("Đã import: " + movie.getName());
                movieRepository.flush(); // Flush sau mỗi save
            } catch (Exception e) {
                System.out.println("❌ Lỗi khi import slug " + slug + ": " + e.getMessage());
            }
        });
    }

    @Async
    public void updateCallData() {
        SlugSets sets = checkSlug();
        Set<String> duplicates = sets.duplicates();
        Set<String> duplicateAndNotCompleted = movieRepository.findBySlugInAndStatusIgnoreCase(duplicates, "ongoing");

        if (duplicateAndNotCompleted.isEmpty()) {
            System.out.println("✅ Không có data mới để update!");
            return;
        }

        duplicateAndNotCompleted.forEach(slug -> {
            String apiUrl = String.format(MOVIE_DETAIL_URL, slug);
            try {
                MovieCallResponse movie = movieService.updateData(apiUrl);
                System.out.println("Đã update: " + movie.getName());
            } catch (Exception e) {
                System.out.println("❌ Lỗi khi update slug " + slug + ": " + e.getMessage());
            }
        });
    }
}
