package com.rutaaprendizajewebflux.capability.application.mapper;

import com.rutaaprendizajewebflux.capability.application.dto.request.SaveCapabilityPlusTechnologiesRequest;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;

public interface ICapabilityPlusTechnologiesRequestMapper {
    CapabilityPlusTechnologiesModel toModel(SaveCapabilityPlusTechnologiesRequest request);
}
