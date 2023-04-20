package com.linking.global.exception;

import com.linking.global.common.ResponseHandler;
import com.linking.global.message.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validationException(MethodArgumentNotValidException exception) {
        if (exception.getMessage() != null)
            return ResponseHandler.generateResponse(exception.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST, null);
        return ResponseHandler.generateResponse(ErrorMessage.NO_ARGUMENT, HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> noSuchElementException(NoSuchElementException exception) {
        return ResponseHandler.generateResponse(exception.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> numberFormatException(NumberFormatException exception) {
        return ResponseHandler.generateResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(NoAuthorityException.class)
    public ResponseEntity<Object> noAuthorityException(NoAuthorityException exception) {
        return ResponseHandler.generateResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }
}
