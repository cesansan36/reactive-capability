package com.rutaaprendizajewebflux.capability.domain.usecase;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ISaveCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import reactor.core.publisher.Mono;

public class SaveCapabilityUseCase implements ISaveCapabilityServicePort {

    private final ICapabilityPersistencePort capabilityPersistencePort;
    private final ITechnologyCommunicationPort technologyCommunicationPort;

    public SaveCapabilityUseCase(ICapabilityPersistencePort capabilityPersistencePort, ITechnologyCommunicationPort technologyCommunicationPort) {
        this.capabilityPersistencePort = capabilityPersistencePort;
        this.technologyCommunicationPort = technologyCommunicationPort;
    }

    public Mono<CapabilityPlusTechnologiesModel> save(Mono<CapabilityPlusTechnologiesModel> capabilityPlusTechnologiesModel) {

        return null;
    }
}
