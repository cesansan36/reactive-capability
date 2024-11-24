package com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository;

import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.CapabilityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ICapabilityRepository extends ReactiveCrudRepository<CapabilityEntity, Long> {

    Mono<CapabilityEntity> findByName(String name);

    Flux<CapabilityEntity> findByNameIn(List<String> names);
}
