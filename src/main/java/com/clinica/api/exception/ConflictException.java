package com.clinica.api.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String mensagem) {
        super(mensagem);
    }
}