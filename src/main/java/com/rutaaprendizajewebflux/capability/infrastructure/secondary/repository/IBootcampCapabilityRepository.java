package com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository;

import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.BootcampCapabilityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IBootcampCapabilityRepository extends ReactiveCrudRepository<BootcampCapabilityEntity, String> {

    Flux<BootcampCapabilityEntity> findAllByBootcampId(Long bootcampId);
}
