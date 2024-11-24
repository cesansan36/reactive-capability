package com.rutaaprendizajewebflux.capability.domain.usecase;

import com.rutaaprendizajewebflux.capability.domain.exception.CapabilityNotFoundException;
import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ICapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import reactor.core.publisher.Mono;

import static com.rutaaprendizajewebflux.capability.domain.util.ExceptionConstants.CAPABILITY_NOT_FOUND_MESSAGE;

public class SoloCapabilityUseCase implements ICapabilityServicePort {

    private final ICapabilityPersistencePort capabilityPersistencePort;

    public SoloCapabilityUseCase(ICapabilityPersistencePort capabilityPersistencePort) {
        this.capabilityPersistencePort = capabilityPersistencePort;
    }

    @Override
    public Mono<SoloCapabilityModel> findById(Long id) {
        return capabilityPersistencePort
                .findById(id)
                .switchIfEmpty(Mono.error(new CapabilityNotFoundException(CAPABILITY_NOT_FOUND_MESSAGE)));
    }

}
