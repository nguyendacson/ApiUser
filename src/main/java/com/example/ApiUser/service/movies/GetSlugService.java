package com.example.ApiUser.service.movies;

import com.example.ApiUser.dto.response.callData.MovieResponse;
import com.example.ApiUser.dto.response.callData.getSlug.MoviePageResponse;
import com.example.ApiUser.entity.movies.Movie;
import com.example.ApiUser.respository.callData.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Service
public class GetSlugService {
    private static final String BASE_URL = "https://phimapi.com/danh-sach/phim-moi-cap-nhat?page=%d";
    private static final String MOVIE_DETAIL_URL = "https://phimapi.com/phim/%s"; // chi tiết phim theo slug

    private static final int THREADS = 12; // ít thôi để tránh bị block
    private static final int MAX_RETRY = 5; // số lần thử lại khi bị lỗi

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    public Set<String> crawlAllSlug() {

        RestTemplate restTemplate = new RestTemplate();
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        // Gọi page=1 để lấy totalPages
        MoviePageResponse firstPage = restTemplate.getForObject(String.format(BASE_URL, 1), MoviePageResponse.class);
        if (firstPage == null || firstPage.getPagination() == null) {
            System.out.println("Không lấy được dữ liệu!");
            return null;
        }

        int totalPages = firstPage.getPagination().getTotalPages();
        System.out.println("Tổng số trang: " + totalPages);

        // List để chứa slug
        Set<String> allSlugs = ConcurrentHashMap.newKeySet();;

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
        return allSlugs;
    }

    public void createAndUpdate(){
        Set<String> slugs = crawlAllSlug();

        Set<String> existedSlugs = movieRepository.findAll().stream()
                .map(Movie::getSlug)
                .collect(Collectors.toSet());

        Set<String> duplicates = slugs.stream()
                .filter(existedSlugs::contains)
                .collect(Collectors.toSet());
        
        Set<String> duplicateAndNotCompleted = movieRepository.findOngoingSlugsIn(duplicates);

        Set<String> newSlugs = slugs.stream()
                .filter(slug -> !existedSlugs.contains(slug))
                .collect(Collectors.toSet());

        newSlugs.forEach(this::createNewSlug);
        duplicateAndNotCompleted.forEach(this::updateSlug);

//        System.out.println("✅ Slug đã tồn tại àm đang going: " + duplicateAndNotCompleted.size());
//        System.out.println("✅ Slug mới: " + newSlugs.size());
//        System.out.println("✅ Slug mới: " + movieRepository.count());
    }

    private void createNewSlug(String slug) {
        String apiUrl = String.format(MOVIE_DETAIL_URL, slug);
        try {
            MovieResponse movie = movieService.newData(apiUrl);
            System.out.println("Đã import: " + movie.getName());
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi import slug " + slug + ": " + e.getMessage());
        }
    }
    
    private void updateSlug(String slug) {
        String apiUrl = String.format(MOVIE_DETAIL_URL, slug);
        try {
            MovieResponse movie = movieService.newData(apiUrl);
            System.out.println("Đã import: " + movie.getName());
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi import slug " + slug + ": " + e.getMessage());
        }
    }
}
