package com.rutaaprendizajewebflux.capability.application.mapper;

import com.rutaaprendizajewebflux.capability.application.dto.response.SoloCapabilityResponse;
import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;

public interface ISoloCapabilityResponseMapper {
    SoloCapabilityResponse toSoloCapabilityResponse(SoloCapabilityModel soloCapabilityModel);
}
