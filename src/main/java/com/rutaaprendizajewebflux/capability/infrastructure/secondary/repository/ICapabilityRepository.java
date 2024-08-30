package com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository;

import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.CapabilityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ICapabilityRepository extends ReactiveCrudRepository<CapabilityEntity, Long> {

    Mono<CapabilityEntity> findByName(String name);
}
