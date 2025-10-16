package com.example.ApiUser.exception;

import com.example.ApiUser.dto.response.authentication.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse<?>> handlerRuntimeException(RuntimeException runtimeException){
//        ApiResponse<?> apiResponse = new ApiResponse<>();
//
//        apiResponse.setCode(ErrorCode.USER_OTHER_EXCEPTION.getCode());
//        apiResponse.setMessage(ErrorCode.USER_OTHER_EXCEPTION.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<?>> handlerRuntimeException(RuntimeException exception) {
        exception.printStackTrace(); // 👈 thêm dòng này

        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(ErrorCode.USER_OTHER_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.USER_OTHER_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<?>> handlerAppException(AppException appException){

        ErrorCode errorCode = appException.getErrorCode();

        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setSuccess(false);
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlerMethodNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){

        String enumKey = Objects.requireNonNull(methodArgumentNotValidException.getFieldError()).getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_ENUM_KEY;
        Map<String, Objects> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation = methodArgumentNotValidException.getBindingResult()
                    .getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info("Log attributes{}", attributes.toString());

            } catch (IllegalArgumentException _) {
        }

        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) ?
                mapAttributes(errorCode.getMessage(), attributes)
                : errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttributes(String message, Map<String, Objects> attributes){
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

    @ExceptionHandler(value = ParseException.class)
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

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<?>> handAccessDeniedException(){
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse<?>> DataIntegrityViolationException(){
        ErrorCode errorCode = ErrorCode.MYSQL;
        return ResponseEntity.status(errorCode.getStatusCode()).body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }
}
