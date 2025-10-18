package com.example.ApiUser.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum ErrorCode {
    USER_OTHER_EXCEPTION(9999, "Unexpected server error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN_TYPE(8888, "Token not type", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User already exists", HttpStatus.CONFLICT),
    EMAIL_EXISTED(1001, "Email already exists", HttpStatus.CONFLICT),
    EMAIL_NOT_EXISTED(1001, "Email not exists", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(1002, "User does not exist", HttpStatus.NOT_FOUND),
    NAME_INVALID(1003, "Name must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    AUTH_NOT_EXISTED(1005, "Authentication token does not exist", HttpStatus.UNAUTHORIZED),
    USER_NOTFOUND(1006, "User not found", HttpStatus.NOT_FOUND),
    INVALID_ENUM_KEY(1007, "Invalid enum key", HttpStatus.BAD_REQUEST),
    JWT_TOKEN_NOT_VALID(1008, "Invalid or expired JWT token", HttpStatus.UNAUTHORIZED),
    UNAUTHENTICATED(1009, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    MYSQL(1010, "MySQL constraint violation", HttpStatus.CONFLICT),
    INVALID_DOB(1011, "Invalid date of birth {min}", HttpStatus.BAD_REQUEST),

    //    Movie
    MOVIE_NOT_EXISTED(1012, "Movies not existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1013, "Categories not existed", HttpStatus.BAD_REQUEST),
    DATA_MOVIE_NOT_EXISTED(1014, "Data Movies not existed", HttpStatus.BAD_REQUEST),
    ERROR_CREATE_DATA_MOVIE(1015, "Error create Call Data Movie", HttpStatus.BAD_REQUEST),
    USER_MOVIE_EXISTED(1016, "User and Movie existed", HttpStatus.BAD_REQUEST),
    USER_DATA_MOVIE_EXISTED(1017, "User and Data Movie and Movies existed", HttpStatus.BAD_REQUEST),
    EXCEEDED_COMMENT(1018, "You have exceeded the number of comment", HttpStatus.BAD_REQUEST),
    LIKE_NOT_EXISTED(1019, "Likes not existed", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_EXISTED(1019, "Comment by this User not existed", HttpStatus.BAD_REQUEST),
    MOVIE_NOT_COMMENT(1020, "Movie not comment!", HttpStatus.BAD_REQUEST),

    // MysList
    MYLIST_EXISTED(1025, "Movie existed on My List!", HttpStatus.BAD_REQUEST),
    MYLIST_NOT_EXISTED(1026, "Movie not existed on My List!", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1027, "Invalid Request", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1028, "Invalid Token Email", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1029, "Token expired", HttpStatus.BAD_REQUEST),
    FAIL_VERIFIED_MAIL(1029, "Error when verified with your mail", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    int code;
    String message;
    HttpStatus statusCode;
}
