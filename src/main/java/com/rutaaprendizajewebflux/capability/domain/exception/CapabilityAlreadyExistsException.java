package com.rutaaprendizajewebflux.capability.domain.exception;

public class CapabilityAlreadyExistsException extends RuntimeException{

    public CapabilityAlreadyExistsException(String message) {
        super(message);
    }
}
