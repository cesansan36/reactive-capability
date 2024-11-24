package com.rutaaprendizajewebflux.capability.domain.exception;

import com.rutaaprendizajewebflux.capability.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class ValueNotValidException extends CustomException {

    public ValueNotValidException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
