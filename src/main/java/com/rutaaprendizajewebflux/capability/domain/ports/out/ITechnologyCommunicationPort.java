package com.rutaaprendizajewebflux.capability.domain.ports.out;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.webclientobjects.response.CapabilityWithTechnologyResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyCommunicationPort {
    Mono<CapabilityPlusTechnologiesModel> associateTechnologiesWithCapability(CapabilityPlusTechnologiesModel capability);

    Mono<CapabilityPlusTechnologiesModel> getTechnologiesByCapabilityId(Long capabilityId);

    Flux<CapabilityPlusTechnologiesModel> findPaginatedCapabilityIdsByTechnologyAmount(int page, int size, String direction);
}
