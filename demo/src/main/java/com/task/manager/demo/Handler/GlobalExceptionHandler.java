package com.task.manager.demo.Handler;

import com.task.manager.demo.Excepiton.BadRequestExcepiton;
import com.task.manager.demo.Excepiton.ErrorResponse;
import com.task.manager.demo.Excepiton.NotFoundExcepiton;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundExcepiton.class)
    public ResponseEntity<ErrorResponse>handleNotFoundExcepiton(NotFoundExcepiton ex) {
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestExcepiton.class)
    public ResponseEntity<ErrorResponse> handleBadRequestExcepiton(BadRequestExcepiton ex){
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
