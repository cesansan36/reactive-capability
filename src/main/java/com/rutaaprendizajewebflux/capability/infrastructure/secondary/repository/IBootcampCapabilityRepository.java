package com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository;

import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.BootcampCapabilityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBootcampCapabilityRepository extends ReactiveCrudRepository<BootcampCapabilityEntity, String> {

}
