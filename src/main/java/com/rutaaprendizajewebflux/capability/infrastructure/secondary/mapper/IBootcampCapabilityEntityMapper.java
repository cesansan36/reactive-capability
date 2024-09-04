package com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper;

import com.rutaaprendizajewebflux.capability.domain.model.BootcampCapabilityRelationModel;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.BootcampCapabilityEntity;

public interface IBootcampCapabilityEntityMapper {
    BootcampCapabilityRelationModel toModel(BootcampCapabilityEntity entity);

    BootcampCapabilityEntity toEntity(BootcampCapabilityRelationModel model);
}
