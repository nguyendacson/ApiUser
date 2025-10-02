package com.example.ApiUser.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum ErrorCode {
    USER_OTHER_EXCEPTION(9999, "Unexpected server error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User already exists", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(1002, "User does not exist", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    AUTH_NOT_EXISTED(1005, "Authentication token does not exist", HttpStatus.UNAUTHORIZED),
    USER_NOTFOUND(1006, "User not found", HttpStatus.NOT_FOUND),
    INVALID_ENUM_KEY(1007, "Invalid enum key", HttpStatus.BAD_REQUEST),
    JWT_TOKEN_NOT_VALID(1008, "Invalid or expired JWT token", HttpStatus.UNAUTHORIZED),
    UNAUTHENTICATED(1009, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    MYSQL(1010, "MySQL constraint violation", HttpStatus.CONFLICT),
    INVALID_DOB(1011, "Invalid date of birth {min}", HttpStatus.BAD_REQUEST),
    USER_DATA_MOVIE_NOT_EXISTED(1012, "User and Data Movie and Movies not existed", HttpStatus.BAD_REQUEST),

    //    Movie
    MOVIE_NOT_EXISTED(1013,"Movies not existed",HttpStatus.BAD_REQUEST),
    DATA_MOVIE_NOT_EXISTED(1014,"Data Movies not existed",HttpStatus.BAD_REQUEST);


    ;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    int code;
    String message;
    HttpStatus statusCode;
    }
