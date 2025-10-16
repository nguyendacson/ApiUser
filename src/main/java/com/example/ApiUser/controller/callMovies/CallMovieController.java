package com.example.ApiUser.controller.callMovies;

import com.example.ApiUser.dto.response.authentication.ApiResponse;
import com.example.ApiUser.service.callMovies.CallMovieService;
import com.example.ApiUser.service.callMovies.CallSlugService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/callMovie")
@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallMovieController {
    CallSlugService callSlugService;
    CallMovieService callMovieService;

    @PostMapping("/createCallData")
    ApiResponse<String> crawlMovies() {
        try {
            log.info("🚀 Start Create Data Movie...");
            callSlugService.createCallData();
            return ApiResponse.<String>builder()
                    .result("Crawl & import data movie Success!")
                    .build();
        } catch (Exception e) {
            log.error("❌ Error when call Movie: ", e);
            return ApiResponse.<String>builder()
                    .code(1015)
                    .message("error")
                    .result("Error when Call is: " + e.getMessage())
                    .build();
        }
    }

    @PostMapping("/updateCallData")
    ResponseEntity<ApiResponse<String>> updateMovies() {
        try {
            log.info("🚀 Start update All Data Movie...");
            callSlugService.updateCallData();
            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .result("Update data movie Success!")
                            .build()
            );
        } catch (Exception e) {
            log.error("❌ Error when Update movie: ", e);
            return ResponseEntity.internalServerError().body(
                    ApiResponse.<String>builder()
                            .result("Error is: " + e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/deleteCallData/{keySearch}")
    ApiResponse<String> deleteMovies(@PathVariable String keySearch) {
        callMovieService.deleteMovie(keySearch);
        return ApiResponse.<String>builder()
                .message("Delete Movie")
                .result("Delete movie has name " + keySearch)
                .build();
    }
}
