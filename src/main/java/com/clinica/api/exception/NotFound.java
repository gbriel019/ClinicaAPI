package com.clinica.api.exception;

public class NotFound extends RuntimeException {

    public NotFound(String mensagem) {
        super(mensagem);
    }
}
