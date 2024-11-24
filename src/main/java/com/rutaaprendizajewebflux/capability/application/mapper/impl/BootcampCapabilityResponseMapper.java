package com.rutaaprendizajewebflux.capability.application.mapper.impl;

import com.rutaaprendizajewebflux.capability.application.dto.response.BootcampWithCapabilitiesResponse;
import com.rutaaprendizajewebflux.capability.application.dto.response.CapabilityInBootcampResponse;
import com.rutaaprendizajewebflux.capability.application.dto.response.TechnologyInCapabilityResponse;
import com.rutaaprendizajewebflux.capability.application.mapper.IBootcampCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.model.BootcampWithCapabilitiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.Technology;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BootcampCapabilityResponseMapper implements IBootcampCapabilityResponseMapper {

    @Override
    public BootcampWithCapabilitiesResponse toBootcampWithCapabilitiesResponse(BootcampWithCapabilitiesModel bootcampWithCapabilitiesModel) {
        BootcampWithCapabilitiesResponse bootcampWithCapabilitiesResponse = new BootcampWithCapabilitiesResponse();
        bootcampWithCapabilitiesResponse.setBootcampId(bootcampWithCapabilitiesModel.getBootcampId());

        List<CapabilityInBootcampResponse> capabilities = new ArrayList<>();

        bootcampWithCapabilitiesModel
                .getCapabilities()
                .forEach(capability -> {
                    toCapabilityInBootcampResponse(capability);
                    capabilities.add(toCapabilityInBootcampResponse(capability));
                });

        bootcampWithCapabilitiesResponse.setCapabilities(capabilities);
        return bootcampWithCapabilitiesResponse;
    }

    private CapabilityInBootcampResponse toCapabilityInBootcampResponse(CapabilityPlusTechnologiesModel capabilityPlusTechnologiesModel) {
        CapabilityInBootcampResponse capabilityInBootcampResponse = new CapabilityInBootcampResponse();

        capabilityInBootcampResponse.setCapabilityId(capabilityPlusTechnologiesModel.getId());
        capabilityInBootcampResponse.setName(capabilityPlusTechnologiesModel.getName());

        List<TechnologyInCapabilityResponse> technologies = new ArrayList<>();

        capabilityPlusTechnologiesModel
                .getTechnologies()
                .forEach(technology -> {
                    toTechnologyInCapabilityResponse(technology);
                    technologies.add(toTechnologyInCapabilityResponse(technology));
                });

        capabilityInBootcampResponse.setTechnologies(technologies);

        return capabilityInBootcampResponse;
    }

    private TechnologyInCapabilityResponse toTechnologyInCapabilityResponse(Technology technology) {
        TechnologyInCapabilityResponse technologyInCapabilityResponse = new TechnologyInCapabilityResponse();
        technologyInCapabilityResponse.setId(technology.getId());
        technologyInCapabilityResponse.setName(technology.getName());
        return technologyInCapabilityResponse;
    }
}
