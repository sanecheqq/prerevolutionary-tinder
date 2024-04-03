package com.liga.semin.server.controller;

import com.liga.semin.server.exception.ExceptionResponse;
import com.liga.semin.server.exception.UserNotFoundException;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity<Object> handleBadParameterException(Exception e) {
        return handleExceptionInternal(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(Exception e) {
        return handleExceptionInternal(e, HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex, Object body, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode, @NonNull WebRequest request
    ) {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, HttpStatus status) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response, status);
    }

}
