package com.linking.global;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validationError(MethodArgumentNotValidException exception) {
        if (exception.getMessage() != null) {
            return ResponseHandler.generateResponse(exception.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST, null);
        }
        return ResponseHandler.generateResponse(ErrorMessage.NO_ARGUMENT, HttpStatus.BAD_REQUEST, null);
    }
}
