package com.rutaaprendizajewebflux.capability.domain.ports.out;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapabilityPersistencePort {
    Mono<SoloCapabilityModel> findById(Long id);

    Mono<SoloCapabilityModel> findByName(String name);

    Mono<CapabilityPlusTechnologiesModel> save(Mono<CapabilityPlusTechnologiesModel> capabilityPlusTechnologiesModel);

    Flux<CapabilityPlusTechnologiesModel> findAllPaginatedByField(int page, int size, String sortBy, String direction);

    Flux<CapabilityPlusTechnologiesModel> findAllByIds(Flux<Long> ids);

    Flux<CapabilityPlusTechnologiesModel> findAllByNames(List<String> names);
}
