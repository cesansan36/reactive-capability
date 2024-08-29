package com.rutaaprendizajewebflux.capability.application.mapper.impl;

import com.rutaaprendizajewebflux.capability.application.dto.response.SoloCapabilityResponse;
import com.rutaaprendizajewebflux.capability.application.mapper.ISoloCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;

public class SoloCapabilityResponseMapper implements ISoloCapabilityResponseMapper {

    @Override
    public SoloCapabilityResponse toSoloCapabilityResponse(SoloCapabilityModel soloCapabilityModel) {
        return new SoloCapabilityResponse(soloCapabilityModel.getId(), soloCapabilityModel.getName(), soloCapabilityModel.getDescription());
    }
}
