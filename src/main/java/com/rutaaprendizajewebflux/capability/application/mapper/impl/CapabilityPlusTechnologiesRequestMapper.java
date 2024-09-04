package com.rutaaprendizajewebflux.capability.application.mapper.impl;

import com.rutaaprendizajewebflux.capability.application.dto.request.SaveCapabilityPlusTechnologiesRequest;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesRequestMapper;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.Technology;

import java.util.List;

public class CapabilityPlusTechnologiesRequestMapper implements ICapabilityPlusTechnologiesRequestMapper {

    @Override
    public CapabilityPlusTechnologiesModel toModel(SaveCapabilityPlusTechnologiesRequest request) {

        System.out.println("esto es lo que llega? =============================================================");
        System.out.println(request);

        List<Technology> technologies = request
                .getTechnologiesNames()
                .stream()
                .map(technology -> new Technology(null, technology)).toList();

        return new CapabilityPlusTechnologiesModel(
                null,
                request.getName(),
                request.getDescription(),
                technologies);
    }
}
