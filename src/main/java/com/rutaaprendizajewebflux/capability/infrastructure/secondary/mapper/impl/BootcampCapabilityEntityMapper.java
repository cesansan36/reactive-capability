package com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl;

import com.rutaaprendizajewebflux.capability.domain.model.BootcampCapabilityRelationModel;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.BootcampCapabilityEntity;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.IBootcampCapabilityEntityMapper;

public class BootcampCapabilityEntityMapper implements IBootcampCapabilityEntityMapper {

    @Override
    public BootcampCapabilityRelationModel toModel(BootcampCapabilityEntity entity) {
        return new BootcampCapabilityRelationModel(entity.getId(), entity.getBootcampId(), entity.getCapabilityId());
    }

    @Override
    public BootcampCapabilityEntity toEntity(BootcampCapabilityRelationModel model) {
        return new BootcampCapabilityEntity(model.getId(), model.getBootcampId(), model.getCapabilityId());
    }
}
