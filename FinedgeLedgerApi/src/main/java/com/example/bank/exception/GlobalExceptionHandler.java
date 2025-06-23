package com.example.bank.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ---------------- 404 ---------------- */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(NotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    /* ----------- saldo insufficiente ----------- */
    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> insufficient(InsufficientFundsException ex) {
        return Map.of("error", ex.getMessage());
    }

    /* ----------- Bean-Validation (body) -------- */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidBody(MethodArgumentNotValidException ex) {
        FieldError fe = ex.getBindingResult().getFieldErrors().stream()
                .findFirst().orElse(null);
        String msg = fe != null ? fe.getDefaultMessage() : "Dati non validi";
        return Map.of("error", msg);
    }

    /* ----------- Bean-Validation (param / path) -------- */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidParam(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .findFirst().map(cv -> cv.getMessage())
                .orElse("Dati non validi");
        return Map.of("error", msg);
    }
}
