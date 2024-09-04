package com.rutaaprendizajewebflux.capability.infrastructure.secondary.exception;

public class DuplicateRegistryException extends RuntimeException {

    public DuplicateRegistryException(String message) {
        super(message);
    }
}
