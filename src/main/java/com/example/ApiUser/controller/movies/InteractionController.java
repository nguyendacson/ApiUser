package com.example.ApiUser.controller.movies;

import com.example.ApiUser.dto.request.movies.MovieFilterRequest;
import com.example.ApiUser.dto.request.movies.WatchingRequest;
import com.example.ApiUser.dto.request.movies.comments.CreateCommentRequest;
import com.example.ApiUser.dto.request.movies.comments.UpdateCommentRequest;
import com.example.ApiUser.dto.response.authentication.ApiResponse;
import com.example.ApiUser.dto.response.movies.CommentResponse;
import com.example.ApiUser.dto.response.movies.WatchingResponse;
import com.example.ApiUser.entity.movies.MovieDTO;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.service.helper.PaginationHelper;
import com.example.ApiUser.service.movies.interactionService.CommentService;
import com.example.ApiUser.service.movies.interactionService.LikeService;
import com.example.ApiUser.service.movies.interactionService.MyListService;
import com.example.ApiUser.service.movies.interactionService.TrailerService;
import com.example.ApiUser.service.movies.interactionService.listForYou.ListForYouService;
import com.example.ApiUser.service.movies.interactionService.watching.WatchingService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/movies")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InteractionController {
    LikeService likeService;
    CommentService commentService;
    PaginationHelper paginationHelper;
    WatchingService watchingService;
    MyListService myListService;
    ListForYouService listForYouService;
    TrailerService trailerService;

    @PostMapping("/{movieId}/likes")
    ApiResponse<String> likeMovie(@PathVariable String movieId,
                                  @AuthenticationPrincipal Jwt jwt) {
//      String userId = jwt.getClaimAsString("sub");
        String userId = jwt.getClaimAsString("userId");
        likeService.createLike(movieId, userId);

        return ApiResponse.<String>builder()
                .result("You liked movie!")
                .build();
    }

    @DeleteMapping("/{movieId}/likes")
    ApiResponse<String> unlikeMovie(@PathVariable String movieId,
                                    @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        likeService.deleteLike(movieId, userId);
        listForYouService.refreshRecommendations(userId);
        return ApiResponse.<String>builder()
                .result("Like canceled!")
                .build();
    }

    @GetMapping("/likes")
    ApiResponse<List<MovieDTO>> allLikeMovie(@AuthenticationPrincipal Jwt jwt,
                                             @RequestParam(defaultValue = "desc") String sort,
                                             @RequestParam(defaultValue = "createdAt") String sortBy) {
        String userId = jwt.getClaimAsString("userId");
        Sort sortList = paginationHelper.buildSort(sort, sortBy);

        List<MovieDTO> listResponse = likeService.getAllLikeByUser(userId, sortList);

        return ApiResponse.<List<MovieDTO>>builder()
                .result(listResponse)
                .build();
    }

    @PostMapping("/{movieId}/trailers")
    ApiResponse<String> trailerMovie(@PathVariable String movieId,
                                     @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        trailerService.createTrailer(movieId, userId);

        return ApiResponse.<String>builder()
                .result("You added trailer movie!")
                .build();
    }

    @DeleteMapping("/{movieId}/trailers")
    ApiResponse<String> unTrailerMovie(@PathVariable String movieId,
                                       @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        trailerService.deleteTrailer(movieId, userId);
        return ApiResponse.<String>builder()
                .result("Trailer canceled!")
                .build();
    }

    @GetMapping("/trailers")
    ApiResponse<List<MovieDTO>> allTrailerByUser(@AuthenticationPrincipal Jwt jwt,
                                                 @RequestParam(defaultValue = "desc") String sort,
                                                 @RequestParam(defaultValue = "createdAt") String sortBy,
                                                 @RequestParam(required = false) String filter) {
        String userId = jwt.getClaimAsString("userId");
        Sort sortList = paginationHelper.buildSort(sort, sortBy);

        List<MovieDTO> listResponse = trailerService.getAllTrailerUser(userId, sortList, filter);

        return ApiResponse.<List<MovieDTO>>builder()
                .result(listResponse)
                .build();
    }


    @PostMapping("/comments")
    ApiResponse<String> commentMovie(@RequestBody @Valid CreateCommentRequest commentRequest,
                                     @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        if (userId == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        commentService.createComment(commentRequest, userId);

        return ApiResponse.<String>builder()
                .result("Comment added successfully")
                .build();
    }

    @PatchMapping("/update/comments")
    ApiResponse<String> updateCommentMovie(@RequestBody UpdateCommentRequest request,
                                           @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        commentService.updateComment(request, userId);

        return ApiResponse.<String>builder()
                .result("You updated comment success!")
                .build();
    }

    @DeleteMapping("/comments/{commentId}")
    ApiResponse<String> unCommentMovie(@PathVariable String commentId,
                                       @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        commentService.deleteComment(commentId, userId);

        return ApiResponse.<String>builder()
                .result("Comment deleted successfully")
                .build();
    }

    @GetMapping("/{movieId}/comments")
    ApiResponse<List<CommentResponse>> allCommentByMovie(@AuthenticationPrincipal Jwt jwt,
                                                         @PathVariable String movieId,
                                                         @RequestParam(defaultValue = "desc") String sort,
                                                         @RequestParam(defaultValue = "createdAt") String sortBy) {
        Sort listSort = paginationHelper.buildSort(sort, sortBy);
        String currentUserId = jwt.getClaimAsString("userId");
        List<CommentResponse> commentResponses = commentService.allCommentByMovie(movieId, listSort, currentUserId);

        return ApiResponse.<List<CommentResponse>>builder()
                .message("All comment by Movie")
                .result(commentResponses)
                .build();
    }

    @PostMapping("/watchlist")
    ApiResponse<String> createWatchList(@RequestBody @Valid WatchingRequest watchingCreateRequest,
                                        @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        watchingService.createWatching(watchingCreateRequest, userId);
        listForYouService.refreshRecommendations(userId);
        return ApiResponse.<String>builder()
                .result("Add success")
                .build();
    }

    @GetMapping("/watchlist")
    ApiResponse<List<WatchingResponse>> allWatchListByUser(@AuthenticationPrincipal Jwt jwt,
                                                           @RequestParam(required = false) String type) {
        String userId = jwt.getClaimAsString("userId");
        return ApiResponse.<List<WatchingResponse>>builder()
                .result(watchingService.allWatchingByUser(userId,type))
                .build();
    }

    @PostMapping("/{movieId}/myList")
    ApiResponse<String> myListMovie(@PathVariable String movieId,
                                    @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        myListService.createMyList(movieId, userId);

        return ApiResponse.<String>builder()
                .result("Add myList success!")
                .build();
    }

    @DeleteMapping("/{movieId}/myList")
    ApiResponse<String> unMyListMovie(@PathVariable String movieId,
                                      @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        myListService.deleteMyList(movieId, userId);

        return ApiResponse.<String>builder()
                .result("You have removed movie your list.")
                .build();
    }


    @GetMapping("/myList")
    ApiResponse<List<MovieDTO>> allMyListMovie(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "createdAt") String sortBy) {

        String userId = jwt.getClaimAsString("userId");
        Sort sortList = paginationHelper.buildSort(sort, sortBy);
        List<MovieDTO> list = myListService.allMyLists(userId, sortList);

        return ApiResponse.<List<MovieDTO>>builder()
                .result(list)
                .build();
    }

    @GetMapping("/list-for-you")
    public ApiResponse<List<MovieDTO>> listForYou(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer year,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        Pageable pageable = paginationHelper.buildPageable(page, limit, null, null);
        MovieFilterRequest filterRequest = MovieFilterRequest.builder()
                .type(type)
                .year(year)
                .limit(limit)
                .category(category)
                .build();
        List<MovieDTO> movies = listForYouService.getRecommendations(filterRequest, userId, pageable);
        return ApiResponse.<List<MovieDTO>>builder()
                .result(movies)
                .build();
    }
}
