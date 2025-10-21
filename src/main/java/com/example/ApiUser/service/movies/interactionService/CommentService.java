package com.example.ApiUser.service.movies.interactionService;


import com.example.ApiUser.dto.request.movies.comments.CreateCommentRequest;
import com.example.ApiUser.dto.request.movies.comments.UpdateCommentRequest;
import com.example.ApiUser.dto.response.movies.CommentResponse;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.Comment;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.movies.CommentMapper;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.movies.MovieCallRepository;
import com.example.ApiUser.repository.movies.interationMovie.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CommentService {

    MovieCallRepository movieCallRepository;
    CommentRepository commentRepository;
    CommentMapper commentMapper;
    UserRepository userRepository;

    public void createComment(CreateCommentRequest commentRequest, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        Movie movie = movieCallRepository.findById(commentRequest.getMovieId())
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));

        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime enbOfDay = startOfDay.plusDays(1).minusNanos(1);

        long count = commentRepository.countByUserAndMovieAndCreatedAtBetween(user, movie, startOfDay, enbOfDay);
        if (count > 5) {
            throw new AppException(ErrorCode.MOV_EXCEEDED_COMMENT);
        }

        Comment comments = commentMapper.toComment(commentRequest);
        comments.setUser(user);
        comments.setMovie(movie);

        commentRepository.save(comments);

    }

    public void deleteComment(String commentId, String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.USR_UNAUTHENTICATED);
        }
        commentRepository.delete(comment);
    }

    public void updateComment(UpdateCommentRequest updateCommentRequest, String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        Comment comments = commentRepository.findById(updateCommentRequest.getCommentId())
                .orElseThrow(() -> new AppException(ErrorCode.MOV_COMMENT_NOT_FOUND));

        if (!comments.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.USR_UNAUTHENTICATED);
        }

        comments.setContent(updateCommentRequest.getNewContent());
        commentRepository.save(comments);
    }

    public List<CommentResponse> allCommentByMovie(String movieId, Sort sort, String currentUserId) {
        Movie movie = movieCallRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));

        List<Comment> comments = commentRepository.findAllByMovie(movie, sort);
        if (comments.isEmpty()) {
            throw new AppException(ErrorCode.MOV_NOT_COMMENTABLE);
        }

        return comments.stream()
                .map(comment -> commentMapper.toCommentResponse(comment, currentUserId))
                .collect(Collectors.toList());

    }
}