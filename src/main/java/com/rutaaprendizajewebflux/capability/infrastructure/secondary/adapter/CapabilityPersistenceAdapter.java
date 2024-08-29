package com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter;

import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ISoloCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository.ICapabilityRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapabilityPersistenceAdapter implements ICapabilityPersistencePort {

    private final ICapabilityRepository capabilityRepository;
    private final ISoloCapabilityEntityMapper soloCapabilityEntityMapper;

    @Override
    public Mono<SoloCapabilityModel> findById(Long id) {
        return capabilityRepository
                .findById(id)
                .map(c -> {
                    System.out.println("En el adapter");
                    System.out.println(c);
                    return c;
                })
                .map(soloCapabilityEntityMapper::toSoloModel);
    }
}
