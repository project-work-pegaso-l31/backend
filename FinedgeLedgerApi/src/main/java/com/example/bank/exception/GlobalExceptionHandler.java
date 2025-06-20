package com.example.bank.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Map<String,String> handleNotFound(NotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    // ⬇︎ nuovo: gestisce @Valid su DTO (@Pattern IBAN, @NotBlank, ecc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleInvalid(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Map.of("error", msg);
    }

    // ⬇︎ nuovo: gestisce @Positive sui @RequestParam (amount)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String,String> handleConstraint(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getMessage())
                .collect(Collectors.joining(", "));
        return Map.of("error", msg);
    }
}
