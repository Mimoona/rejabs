package com.rejabsbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnAuthorizedUserException.class)
    public ResponseEntity<ExceptionMessage> handleUnauthorizedUserException(UnAuthorizedUserException e) {
        ExceptionMessage error = new ExceptionMessage(e.getMessage(),
                Instant.now(),
                HttpStatus.UNAUTHORIZED.name());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}