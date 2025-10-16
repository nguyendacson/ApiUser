package com.example.ApiUser.mapper.movies;


import com.example.ApiUser.dto.request.movies.comments.CreateCommentRequest;
import com.example.ApiUser.dto.response.movies.CommentResponse;
import com.example.ApiUser.entity.movies.Comment;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toComment(CreateCommentRequest commentRequest);

    @Mapping(target = "user", source = "user.id")
    @Mapping(target = "movie", source = "movie.id")
    @Mapping(target = "owner", expression = "java(comment.getUser().getId().equals(currentUserId))")
    CommentResponse toCommentResponse(Comment comment, @Context String currentUserId);
}
