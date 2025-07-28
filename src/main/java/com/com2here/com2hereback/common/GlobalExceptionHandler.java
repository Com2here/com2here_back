package com.com2here.com2hereback.common;

import jakarta.validation.ConstraintViolationException;

import java.nio.file.AccessDeniedException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CMResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        StringBuilder message = new StringBuilder();
        bindingResult.getFieldErrors().forEach(error -> {
            message.append("[")
                    .append(error.getField())
                    .append("] ")
                    .append(error.getDefaultMessage())
                    .append(" ");
        });

        BaseResponseStatus status = BaseResponseStatus.VALIDATION_ERROR;

        return ResponseEntity
                .status(status.getHttpStatusCode())
                .body(CMResponse.fail(status, message.toString().trim()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CMResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        BaseResponseStatus status = BaseResponseStatus.VALIDATION_ERROR;
        return ResponseEntity
                .status(status.getHttpStatusCode())
                .body(CMResponse.fail(status, ex.getMessage()));
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<CMResponse<Void>> handleAccessDenied(Exception ex) {
        BaseResponseStatus status = BaseResponseStatus.FORBIDDEN;
        return ResponseEntity
                .status(status.getHttpStatusCode())
                .body(CMResponse.fail(status));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CMResponse<Void>> handleBaseException(BaseException ex) {
        BaseResponseStatus status = ex.getErrorCode();
        return ResponseEntity
                .status(status.getHttpStatusCode())
                .body(CMResponse.fail(status));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CMResponse<Void>> handleException(Exception ex) {
        ex.printStackTrace();
        BaseResponseStatus status = BaseResponseStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(status.getHttpStatusCode())
                .body(CMResponse.fail(status));
    }
}
