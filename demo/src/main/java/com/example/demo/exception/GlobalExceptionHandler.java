package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
     
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {

        logger.warn("Warn level EntityNotFoundException with message only: {}", ex.getMessage()); 
        logger.error("Error level EntityNotFoundException with message only: {}", ex.getMessage());
        logger.info("Info level EntityNotFoundException with message only: {}", ex.getMessage());
        logger.debug("Debug level EntityNotFoundException with message only: {}", ex.getMessage());
        logger.trace("Trace level EntityNotFoundException with message only: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        logger.error("Error level Unhandled exception with message and stack trace: {}", ex.getMessage(), ex); 
        logger.warn("Warn level Unhandled exception with message and stack trace: {}", ex.getMessage(), ex);
        logger.info("Info level Unhandled exception with message and stack trace: {}", ex.getMessage(), ex);
        logger.trace("Trace level Unhandled exception with message and stack trace: {}", ex.getMessage(), ex);
        logger.debug("Debug level Unhandled exception with message and stack trace: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation failed with warn level: {}", ex.getBindingResult().getFieldErrors());
        log.error("Validation failed with error level: {}", ex.getBindingResult().getFieldErrors());
        log.debug("Validation failed with debug level: {}", ex.getBindingResult().getFieldErrors());
        log.trace("Validation failed with trace level: {}", ex.getBindingResult().getFieldErrors());
        log.info("Validation failed with info level: {}", ex.getBindingResult().getFieldErrors());

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


}