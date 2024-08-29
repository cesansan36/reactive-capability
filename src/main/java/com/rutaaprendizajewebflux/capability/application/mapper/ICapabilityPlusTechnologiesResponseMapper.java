package com.rutaaprendizajewebflux.capability.application.mapper;

import com.rutaaprendizajewebflux.capability.application.dto.response.SaveCapabilityPlusTechnologiesResponse;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;

public interface ICapabilityPlusTechnologiesResponseMapper {
    SaveCapabilityPlusTechnologiesResponse toResponse(CapabilityPlusTechnologiesModel model);
}
