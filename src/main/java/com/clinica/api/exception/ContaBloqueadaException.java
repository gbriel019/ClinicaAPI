package com.clinica.api.exception;

public class ContaBloqueadaException extends RuntimeException {
    public ContaBloqueadaException(String mensagem) {
        super(mensagem);
    }
}