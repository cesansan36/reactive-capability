package com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.CapabilityEntity;

public interface ICapabilityPlusTechnologyEntityMapper {
    CapabilityPlusTechnologiesModel toModel(CapabilityEntity entity);

    CapabilityEntity toEntity(CapabilityPlusTechnologiesModel model);
}
