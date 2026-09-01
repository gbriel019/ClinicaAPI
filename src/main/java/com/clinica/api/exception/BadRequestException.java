package com.clinica.api.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String mensagem) {
        super(mensagem);
    }
}
