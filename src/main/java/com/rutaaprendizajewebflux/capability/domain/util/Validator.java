package com.rutaaprendizajewebflux.capability.domain.util;

import com.rutaaprendizajewebflux.capability.domain.exception.ValueNotValidException;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;

import static com.rutaaprendizajewebflux.capability.domain.util.ExceptionConstants.MAXIMUM_AMOUNT_TECHNOLOGIES_EXCEPTION;
import static com.rutaaprendizajewebflux.capability.domain.util.ExceptionConstants.MINIMUM_AMOUNT_TECHNOLOGIES_EXCEPTION;

public final class Validator {

    private Validator() {}

    public static CapabilityPlusTechnologiesModel validate(CapabilityPlusTechnologiesModel capabilityPlusTechnologiesModel) {

        capabilityPlusTechnologiesModel.removeDuplicatedTechnologiesByName();
        validateMinimumAmountOfTechnologies(capabilityPlusTechnologiesModel);
        validateMaximumAmountOfTechnologies(capabilityPlusTechnologiesModel);
        return capabilityPlusTechnologiesModel;
    }

    private static void validateMaximumAmountOfTechnologies(CapabilityPlusTechnologiesModel capabilityPlusTechnologiesModel) {
        if (capabilityPlusTechnologiesModel.getTechnologies().size() > DomainConstants.MAX_TECHNOLOGIES) {
            throw new ValueNotValidException(MAXIMUM_AMOUNT_TECHNOLOGIES_EXCEPTION.formatted(DomainConstants.MAX_TECHNOLOGIES));
        }
    }

    private static void validateMinimumAmountOfTechnologies(CapabilityPlusTechnologiesModel capabilityPlusTechnologiesModel) {
        if (capabilityPlusTechnologiesModel.getTechnologies().size() < DomainConstants.MIN_TECHNOLOGIES) {
            throw new ValueNotValidException(MINIMUM_AMOUNT_TECHNOLOGIES_EXCEPTION.formatted(DomainConstants.MIN_TECHNOLOGIES));
        }
    }
}
