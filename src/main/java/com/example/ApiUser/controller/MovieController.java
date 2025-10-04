package com.example.ApiUser.controller;

import com.example.ApiUser.dto.request.movies.WatchingCreateRequest;
import com.example.ApiUser.dto.response.ApiResponse;
import com.example.ApiUser.dto.response.movies.WatchingResponse;
import com.example.ApiUser.service.movies.GetSlugService;
import com.example.ApiUser.service.movies.MovieService;
import com.example.ApiUser.service.movies.WatchingListService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/callMovie")
@RestController
@Slf4j
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private WatchingListService watchingListService;
    @Autowired
    private GetSlugService getSlugService;

    @PostMapping("/createCallData")
    ApiResponse<String> crawlMovies() {
        try {
            log.info("🚀 Start Create Data Movie...");
            getSlugService.createCallData();
            return ApiResponse.<String>builder()
                    .result("Crawl & import data movie Success!")
                    .build();
        } catch (Exception e) {
            log.error("❌ Lỗi khi crawl phim", e);
            return ApiResponse.<String>builder()
                    .code(1015)
                    .message("error")
                    .result("Error when call data Movie: " + e.getMessage())
                    .build();
        }
    }

    @PostMapping("/updateCallData")
    ResponseEntity<ApiResponse<String>> updateMovies() {
        try {
            log.info("🚀 Start update All Data Movie...");
            getSlugService.updateCallData();
            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .result("Crawl & import data movie Success!")
                            .build()
            );
        } catch (Exception e) {
            log.error("❌ Lỗi khi crawl phim", e);
            return ResponseEntity.internalServerError().body(
                    ApiResponse.<String>builder()
                            .result("Error when call data Movie" + e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/watchlist/{userId}")
    ApiResponse<List<WatchingResponse>> getWatchList(@PathVariable String userId) {
        return ApiResponse.<List<WatchingResponse>>builder()
                .result(watchingListService.getListWatching(userId))
                .build();
    }

    @PostMapping("/watchlist")
    ApiResponse<WatchingResponse> createWatchList(@RequestBody @Valid WatchingCreateRequest watchingCreateRequest) {
        return ApiResponse.<WatchingResponse>builder()
                .result(watchingListService.createWatchingList(watchingCreateRequest))
                .build();
    }

}
