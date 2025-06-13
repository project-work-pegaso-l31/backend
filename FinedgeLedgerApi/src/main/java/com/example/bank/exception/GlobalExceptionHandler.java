package com.example.bank.exception;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public Map<String,String> handle(NotFoundException ex){
        return Map.of("error", ex.getMessage());
    }
}
