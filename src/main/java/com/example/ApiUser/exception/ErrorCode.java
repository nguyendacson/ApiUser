package com.example.ApiUser.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum ErrorCode {

    USER_EXISTED(1001,"User existed"),
    USER_NOT_EXISTED(1001,"User not existed"),
    AUTH_NOT_EXISTED(1001,"User not existed token"),
    USER_NOTFOUND(1001,"User not found"),
    USER_OTHER_EXCEPTION(9999,"Error Exception"),
    USERNAME_INVALID(1003,"Username must be at least 3 charactor"),
    PASSWORD_INVALID(1004,"Password must be at least 8 charactor"),
    INVALID_ENUM_KEY(1005,"Invalid message key"),
    JWT_TOKEN_NOT_VALID(1006,"Payload of JWS object is not a valid JSON object"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

     int code;
     String message;
}
