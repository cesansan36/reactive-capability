package com.rutaaprendizajewebflux.capability.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Validator {
    private Validator() {}

    public static int validateWholeNumber(String value, int min) {
        try {
            int number = Integer.parseInt(value);
            return Math.max(min, number);
        } catch (NumberFormatException e) {
            log.error("Error parsing number: {}", e.getMessage());
            return min;
        }
    }
}
