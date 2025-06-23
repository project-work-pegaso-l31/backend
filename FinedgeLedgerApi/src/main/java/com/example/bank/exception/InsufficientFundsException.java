package com.example.bank.exception;

/** Lanciata quando si tenta un addebito oltre il saldo disponibile. */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String msg) { super(msg); }
}
