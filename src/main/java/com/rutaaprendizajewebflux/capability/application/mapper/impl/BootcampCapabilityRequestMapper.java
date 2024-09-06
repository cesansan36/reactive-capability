package com.rutaaprendizajewebflux.capability.application.mapper.impl;

import com.rutaaprendizajewebflux.capability.application.dto.request.LinkBootcampWithCapabilitiesRequest;
import com.rutaaprendizajewebflux.capability.application.mapper.IBootcampCapabilityRequestMapper;
import com.rutaaprendizajewebflux.capability.domain.model.LinkedBootcampCapabilityModel;

public class BootcampCapabilityRequestMapper implements IBootcampCapabilityRequestMapper {

    @Override
    public LinkedBootcampCapabilityModel toBootcampModel(LinkBootcampWithCapabilitiesRequest request) {

        return new LinkedBootcampCapabilityModel(
                request.getBootcampId(),
                request.getCapabilitiesNames()
        );
    }
}
