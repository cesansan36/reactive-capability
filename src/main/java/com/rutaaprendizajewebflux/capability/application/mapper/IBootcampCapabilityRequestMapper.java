package com.rutaaprendizajewebflux.capability.application.mapper;

import com.rutaaprendizajewebflux.capability.application.dto.request.LinkBootcampWithCapabilitiesRequest;
import com.rutaaprendizajewebflux.capability.domain.model.LinkedBootcampCapabilityModel;

public interface IBootcampCapabilityRequestMapper {
    LinkedBootcampCapabilityModel toBootcampModel(LinkBootcampWithCapabilitiesRequest request);
}
