package com.example.ApiUser.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum ErrorCode {

    USER_OTHER_EXCEPTION(9999,"Error Exception",HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001,"User existed",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002,"User not existed",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003,"Username must be at least 3 charactor",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004,"Password must be at least 8 charactor",HttpStatus.BAD_REQUEST),
    AUTH_NOT_EXISTED(1005,"User not existed token",HttpStatus.BAD_REQUEST),
    USER_NOTFOUND(1006,"User not found",HttpStatus.NOT_FOUND),
    INVALID_ENUM_KEY(1007,"Invalid message key",HttpStatus.BAD_REQUEST),
    JWT_TOKEN_NOT_VALID(1008,"Payload of JWS object is not a valid JSON object",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1009,"Unauthenticated",HttpStatus.UNAUTHORIZED)
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
