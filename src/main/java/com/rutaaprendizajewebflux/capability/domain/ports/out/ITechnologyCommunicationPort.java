package com.rutaaprendizajewebflux.capability.domain.ports.out;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import reactor.core.publisher.Mono;

public interface ITechnologyCommunicationPort {
    Mono<Void> associateTechnologiesWithCapability(CapabilityPlusTechnologiesModel capability);

    Mono<CapabilityPlusTechnologiesModel> getTechnologiesByCapabilityId(Long capabilityId);
}
