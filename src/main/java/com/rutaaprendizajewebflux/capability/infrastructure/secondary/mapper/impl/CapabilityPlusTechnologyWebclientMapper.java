package com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.Technology;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyWebclientMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.webclientobjects.request.CapabilityWithTechnologyRequest;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.webclientobjects.response.CapabilityWithTechnologyResponse;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.webclientobjects.response.TechnologyResponse;

import java.util.List;

public class CapabilityPlusTechnologyWebclientMapper implements ICapabilityPlusTechnologyWebclientMapper {

    @Override
    public CapabilityWithTechnologyRequest toRequest(CapabilityPlusTechnologiesModel model) {
        CapabilityWithTechnologyRequest request = new CapabilityWithTechnologyRequest();
        request.setCapabilityId(model.getId());
        request.setTechnologiesNames(model.getTechnologiesNames());
        return request;
    }

    @Override
    public CapabilityPlusTechnologiesModel toModel(CapabilityWithTechnologyResponse capabilityWithTechnologyResponse) {

        List<Technology> technologies = capabilityWithTechnologyResponse
                .getTechnologies()
                .stream()
                .map(this::toTechnologyModel)
                .toList();

        return new CapabilityPlusTechnologiesModel(capabilityWithTechnologyResponse.getCapabilityId(),
                null,
                null,
                technologies);
    }

    private Technology toTechnologyModel (TechnologyResponse technologyResponse) {
        return new Technology(technologyResponse.getId(), technologyResponse.getName());
    }
}
