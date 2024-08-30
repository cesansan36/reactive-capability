package com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ISoloCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository.ICapabilityRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapabilityPersistenceAdapter implements ICapabilityPersistencePort {

    private final ICapabilityRepository capabilityRepository;
    private final ISoloCapabilityEntityMapper soloCapabilityEntityMapper;
    private final ICapabilityPlusTechnologyEntityMapper capabilityPlusTechnologyEntityMapper;

    @Override
    public Mono<SoloCapabilityModel> findById(Long id) {
        return capabilityRepository
                .findById(id)
                .map(soloCapabilityEntityMapper::toSoloModel);
    }

    @Override
    public Mono<SoloCapabilityModel> findByName(String name) {
        return capabilityRepository
                .findByName(name).map(soloCapabilityEntityMapper::toSoloModel);
    }


    @Override
    public Mono<CapabilityPlusTechnologiesModel> save(Mono<CapabilityPlusTechnologiesModel> capabilityPlusTechnologiesModel) {
        return capabilityPlusTechnologiesModel
                .map(capabilityPlusTechnologyEntityMapper::toEntity)
                .flatMap(capabilityRepository::save)
                .map(capabilityPlusTechnologyEntityMapper::toModel);
    }
}
