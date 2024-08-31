package com.rutaaprendizajewebflux.capability.application.mapper;

import com.rutaaprendizajewebflux.capability.application.dto.response.CapabilityPlusTechnologiesResponse;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;

public interface ICapabilityPlusTechnologiesResponseMapper {
    CapabilityPlusTechnologiesResponse toResponse(CapabilityPlusTechnologiesModel model);
}
