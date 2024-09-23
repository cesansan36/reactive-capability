package com.rutaaprendizajewebflux.capability.domain.exception;

import com.rutaaprendizajewebflux.capability.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class CapabilityNotFoundException extends CustomException {

    public CapabilityNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
