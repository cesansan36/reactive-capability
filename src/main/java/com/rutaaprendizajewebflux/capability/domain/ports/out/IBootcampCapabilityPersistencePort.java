package com.rutaaprendizajewebflux.capability.domain.ports.out;

import com.rutaaprendizajewebflux.capability.domain.model.BootcampCapabilityRelationModel;
import reactor.core.publisher.Flux;

public interface IBootcampCapabilityPersistencePort {
    Flux<BootcampCapabilityRelationModel> saveAll(Flux<BootcampCapabilityRelationModel> bootcampCapabilityRelations);

    Flux<BootcampCapabilityRelationModel> findAllByBootcampId(Long bootcampId);

    Flux<Long> findPaginatedBootcampIdsByCapabilityAmount(int page, int size, String direction);
}
