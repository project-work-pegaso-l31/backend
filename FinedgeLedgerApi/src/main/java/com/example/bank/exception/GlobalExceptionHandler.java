package com.example.bank.exception;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* NotFound → 404 */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = org.springframework.http.HttpStatus.NOT_FOUND)
    public Map<String,String> handleNotFound(NotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    /* Validazione + logica business → 400 */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = org.springframework.http.HttpStatus.BAD_REQUEST)
    public Map<String,String> handleIllegal(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }
}
