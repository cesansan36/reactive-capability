package com.rutaaprendizajewebflux.capability.domain.exception;

import com.rutaaprendizajewebflux.capability.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class CapabilityAlreadyExistsException extends CustomException {

    public CapabilityAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
