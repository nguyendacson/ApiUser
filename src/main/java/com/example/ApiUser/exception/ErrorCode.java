package com.example.ApiUser.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum ErrorCode {
    // ====== SYS (System & Internal) ======
    SYS_UNEXPECTED_ERROR(9000, "Unexpected server error", HttpStatus.INTERNAL_SERVER_ERROR),
    SYS_INVALID_TOKEN_TYPE(9001, "Invalid token type", HttpStatus.BAD_REQUEST),
    SYS_DATABASE_ERROR(9002, "Database constraint violation", HttpStatus.CONFLICT),

    // ====== USR (User & Authentication) ======
    USR_EXISTED(1000, "User already exists", HttpStatus.CONFLICT),
    USR_EMAIL_EXISTED(1001, "Email already exists", HttpStatus.CONFLICT),
    USR_EMAIL_NOT_FOUND(1002, "Email not found", HttpStatus.NOT_FOUND),
    USR_NOT_FOUND(1003, "User not found", HttpStatus.NOT_FOUND),
    USR_NAME_INVALID(1004, "Name must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USR_USERNAME_INVALID(1005, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USR_PASSWORD_INVALID(1006, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USR_INVALID_DOB(1007, "Invalid date of birth (must be after {min})", HttpStatus.BAD_REQUEST),
    USR_AUTH_TOKEN_MISSING(1008, "Authentication token missing", HttpStatus.UNAUTHORIZED),
    USR_JWT_INVALID(1009, "Invalid or expired JWT token", HttpStatus.UNAUTHORIZED),
    USR_INVALID_ENUM_KEY(1010, "Invalid enum key", HttpStatus.BAD_REQUEST),
    USR_UNAUTHENTICATED(1011, "Unauthenticated request", HttpStatus.UNAUTHORIZED),

    // ====== MOV (Movie & Category) ======
    MOV_NOT_FOUND(2000, "Movie not found", HttpStatus.NOT_FOUND),
    MOV_CATEGORY_NOT_FOUND(2001, "Category not found", HttpStatus.NOT_FOUND),
    MOV_DATA_NOT_FOUND(2002, "Movie data not found", HttpStatus.NOT_FOUND),
    MOV_USER_EXISTED(2003, "User already added this movie", HttpStatus.CONFLICT),
    MOV_USER_DATA_EXISTED(2004, "User data for this movie already exists", HttpStatus.CONFLICT),
    MOV_EXCEEDED_COMMENT(2005, "Comment limit exceeded", HttpStatus.BAD_REQUEST),
    MOV_LIKE_NOT_FOUND(2006, "Like not found", HttpStatus.NOT_FOUND),
    MOV_COMMENT_NOT_FOUND(2007, "Comment not found", HttpStatus.NOT_FOUND),
    MOV_NOT_COMMENTABLE(2008, "Movie cannot be commented", HttpStatus.BAD_REQUEST),

    // ====== LST (My List) ======
    LST_EXISTED(3000, "Movie already exists in My List", HttpStatus.CONFLICT),
    LST_NOT_FOUND(3001, "Movie not found in My List", HttpStatus.NOT_FOUND),

    // ====== TKN (Token & Verification) ======
    TKN_INVALID_REQUEST(4000, "Invalid Request", HttpStatus.BAD_REQUEST),
    TKN_INVALID(4001, "Invalid email verification token", HttpStatus.BAD_REQUEST),
    TKN_EXPIRED(4002, "Verification token expired", HttpStatus.BAD_REQUEST),
    TKN_VERIFICATION_FAILED(4003, "Error verifying your email", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    int code;
    String message;
    HttpStatus statusCode;
}