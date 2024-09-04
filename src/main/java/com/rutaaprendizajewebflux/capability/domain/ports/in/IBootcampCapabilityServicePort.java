package com.rutaaprendizajewebflux.capability.domain.ports.in;

import com.rutaaprendizajewebflux.capability.domain.model.BootcampWithCapabilitiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.LinkedBootcampCapabilityModel;
import reactor.core.publisher.Mono;

public interface IBootcampCapabilityServicePort {
    Mono<BootcampWithCapabilitiesModel> saveAll(Mono<LinkedBootcampCapabilityModel> linkedModel);
}
