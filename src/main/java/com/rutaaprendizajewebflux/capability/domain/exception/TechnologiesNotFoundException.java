package com.rutaaprendizajewebflux.capability.domain.exception;

public class TechnologiesNotFoundException  extends RuntimeException{

    public TechnologiesNotFoundException(String message) {
        super(message);
    }
}
