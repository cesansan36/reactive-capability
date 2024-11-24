package com.rutaaprendizajewebflux.capability.infrastructure.secondary.exception;

import com.rutaaprendizajewebflux.capability.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateRegistryException extends CustomException {

    public DuplicateRegistryException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
