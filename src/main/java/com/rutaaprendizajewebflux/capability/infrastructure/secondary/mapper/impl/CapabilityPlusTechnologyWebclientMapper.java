package com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyWebclientMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.webclientobjects.SaveCapabilityWithTechnologyRequest;

public class CapabilityPlusTechnologyWebclientMapper implements ICapabilityPlusTechnologyWebclientMapper {

    @Override
    public SaveCapabilityWithTechnologyRequest toRequest(CapabilityPlusTechnologiesModel model) {
        SaveCapabilityWithTechnologyRequest request = new SaveCapabilityWithTechnologyRequest();
        request.setCapabilityId(model.getId());
        request.setTechnologiesNames(model.getTechnologiesNames());
        return request;
    }
}
