package com.rutaaprendizajewebflux.capability.domain.ports.out;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;
import reactor.core.publisher.Mono;

public interface ICapabilityPersistencePort {
    Mono<SoloCapabilityModel> findById(Long id);

    Mono<CapabilityPlusTechnologiesModel> save(Mono<CapabilityPlusTechnologiesModel> capabilityPlusTechnologiesModel);
}
