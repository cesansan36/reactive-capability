package com.rutaaprendizajewebflux.capability.domain.ports.in;

import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;
import reactor.core.publisher.Mono;

public interface ICapabilityServicePort {
    Mono<SoloCapabilityModel> findById(Long id);
}
