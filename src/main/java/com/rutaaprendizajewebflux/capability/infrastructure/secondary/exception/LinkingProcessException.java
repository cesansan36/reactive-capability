package com.rutaaprendizajewebflux.capability.infrastructure.secondary.exception;

import com.rutaaprendizajewebflux.capability.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class LinkingProcessException extends CustomException {

    public LinkingProcessException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
