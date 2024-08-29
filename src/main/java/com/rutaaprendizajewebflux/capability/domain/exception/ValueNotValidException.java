package com.rutaaprendizajewebflux.capability.domain.exception;

public class ValueNotValidException extends RuntimeException {

    public ValueNotValidException(String message) {
        super(message);
    }
}
