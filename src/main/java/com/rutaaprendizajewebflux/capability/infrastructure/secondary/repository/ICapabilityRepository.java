package com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository;

import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.CapabilityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICapabilityRepository extends ReactiveCrudRepository<CapabilityEntity, Long> {
}
