package com.rutaaprendizajewebflux.capability.application.mapper.impl;

import com.rutaaprendizajewebflux.capability.application.dto.response.CapabilityPlusTechnologiesResponse;
import com.rutaaprendizajewebflux.capability.application.dto.response.TechnologyInCapabilityResponse;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;

import java.util.List;

public class CapabilityPlusTechnologiesResponseMapper implements ICapabilityPlusTechnologiesResponseMapper {

    @Override
    public CapabilityPlusTechnologiesResponse toResponse(CapabilityPlusTechnologiesModel model) {
        List<TechnologyInCapabilityResponse> technologiesResponse = model
                .getTechnologies().stream().map(technology -> new TechnologyInCapabilityResponse(
                        technology.getId(),
                        technology.getName()
                )).toList();

        return new CapabilityPlusTechnologiesResponse(
                model.getId(),
                model.getName(),
                model.getDescription(),
                technologiesResponse
        );
    }
}
