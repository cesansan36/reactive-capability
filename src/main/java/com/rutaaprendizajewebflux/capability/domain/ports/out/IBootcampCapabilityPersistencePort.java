package com.rutaaprendizajewebflux.capability.domain.ports.out;

import com.rutaaprendizajewebflux.capability.domain.model.BootcampCapabilityRelationModel;
import reactor.core.publisher.Flux;

public interface IBootcampCapabilityPersistencePort {
    Flux<BootcampCapabilityRelationModel> saveAll(Flux<BootcampCapabilityRelationModel> bootcampCapabilityRelations);
}
