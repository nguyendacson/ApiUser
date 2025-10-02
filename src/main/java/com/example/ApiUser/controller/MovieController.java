package com.example.ApiUser.controller;

import com.example.ApiUser.dto.request.movies.WatchingCreateRequest;
import com.example.ApiUser.dto.response.ApiResponse;
import com.example.ApiUser.dto.response.callData.MovieResponse;
import com.example.ApiUser.dto.response.movies.WatchingResponse;
import com.example.ApiUser.service.movies.GetSlugService;
import com.example.ApiUser.service.movies.MovieService;
import com.example.ApiUser.service.movies.WatchingListService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/test")
@RestController
@Slf4j
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private WatchingListService watchingListService;
    @Autowired
    private GetSlugService getSlugService;

    @PostMapping("/createAndUpdate")
    public ResponseEntity<?> crawlMovies() {
        try {
            log.info("🚀 Bắt đầu crawl toàn bộ phim...");
            getSlugService.createAndUpdate();
            return ResponseEntity.ok("✅ Crawl & import phim thành công!");
        } catch (Exception e) {
            log.error("❌ Lỗi khi crawl phim", e);
            return ResponseEntity.internalServerError().body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/watchlist/{userId}")
    ApiResponse<List<WatchingResponse>> getWatchList(@PathVariable String userId) {
        return ApiResponse.<List<WatchingResponse>>builder()
                .result(watchingListService.getListWatching(userId))
                .build();
    }

    @PostMapping("/watchlist")
    ApiResponse<WatchingResponse> createWatchList(@RequestBody @Valid WatchingCreateRequest watchingCreateRequest){
        return ApiResponse.<WatchingResponse>builder()
                .result(watchingListService.createWatchingList(watchingCreateRequest))
                .build();
    }

}
