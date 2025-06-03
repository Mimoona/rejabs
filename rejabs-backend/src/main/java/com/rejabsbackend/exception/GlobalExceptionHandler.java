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
        ExceptionMessage error = new ExceptionMessage("Error: " + e.getMessage(),
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.name());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleIdNotFoundException(IdNotFoundException e){
        ExceptionMessage error = new ExceptionMessage("Error: "+e.getMessage(),
                Instant.now(),
                HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedUserException.class)
    public ResponseEntity<ExceptionMessage> handleUnAuthorizedUserException(UnAuthorizedUserException e){
        ExceptionMessage error = new ExceptionMessage("Error: "+e.getMessage(),
                Instant.now(),
                HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }






}