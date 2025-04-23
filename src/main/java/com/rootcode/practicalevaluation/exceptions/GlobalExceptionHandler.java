package com.rootcode.practicalevaluation.exceptions;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public StandardResponse handleResourceNotFound(ResourceNotFoundException ex) {
        return new StandardResponse("404", ex.getMessage(), null);
    }

    @ExceptionHandler(BadRequestException.class)
    public StandardResponse handleBadRequest(BadRequestException ex) {
        return new StandardResponse("400", ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public StandardResponse handleGenericException(Exception ex) {
        ex.printStackTrace(); // Optional: Log this properly
        return new StandardResponse("500", "Internal server error", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .reduce((first, second) -> first + ", " + second)
                .orElse("Validation error");

        StandardResponse response = new StandardResponse("400", errorMessage, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}