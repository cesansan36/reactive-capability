package com.rutaaprendizajewebflux.capability.domain.exception;

import com.rutaaprendizajewebflux.capability.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class TechnologiesNotFoundException  extends CustomException {

    public TechnologiesNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
