package com.example.ApiUser.exception;

import com.example.ApiUser.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<?>> handlerRuntimeException(RuntimeException runtimeException){
        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(ErrorCode.USER_OTHER_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.USER_OTHER_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<?>> handlerAppException(AppException appException){

        ErrorCode errorCode = appException.getErrorCode();

        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlerMethodNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){

        String enumKey = Objects.requireNonNull(methodArgumentNotValidException.getFieldError()).getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_ENUM_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
            } catch (IllegalArgumentException _) {

        }

        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value =  ParseException.class)
    ResponseEntity<ApiResponse<?>> handlerParseException(ParseException parseException){
        String enumKey = Objects.requireNonNull(parseException.getMessage());
        ApiResponse<?> apiResponse = new ApiResponse<>();

        ErrorCode errorCode = ErrorCode.JWT_TOKEN_NOT_VALID;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException _) {

        }

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
