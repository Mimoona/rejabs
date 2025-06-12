package com.rejabsbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleUnknownException(Exception e){
        ExceptionMessage error = new ExceptionMessage( e.getMessage(),
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.name());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleIdNotFoundException(IdNotFoundException e){
        ExceptionMessage error = new ExceptionMessage(e.getMessage(),
                Instant.now(),
                HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionMessage> handleAuthenticationException(AuthenticationException e){
        ExceptionMessage error = new ExceptionMessage(e.getMessage(),
                Instant.now(),
                HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }







}