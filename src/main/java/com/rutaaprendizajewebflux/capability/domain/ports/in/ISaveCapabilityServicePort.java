package com.rutaaprendizajewebflux.capability.domain.ports.in;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import reactor.core.publisher.Mono;

public interface ISaveCapabilityServicePort {
    Mono<CapabilityPlusTechnologiesModel> save(Mono<CapabilityPlusTechnologiesModel> capabilityPlusTechnologiesModel);
}
