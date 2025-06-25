package com.example.bank.exception;

public class DuplicateFiscalCodeException extends RuntimeException {
    public DuplicateFiscalCodeException(String msg) { super(msg); }
}
