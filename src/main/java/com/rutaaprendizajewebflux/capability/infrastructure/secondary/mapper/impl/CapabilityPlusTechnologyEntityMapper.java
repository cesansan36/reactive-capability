package com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.CapabilityEntity;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyEntityMapper;

import java.util.ArrayList;

public class CapabilityPlusTechnologyEntityMapper implements ICapabilityPlusTechnologyEntityMapper {

    @Override
    public CapabilityPlusTechnologiesModel toModel(CapabilityEntity entity) {

        return new CapabilityPlusTechnologiesModel(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                new ArrayList<>());
    }

    @Override
    public CapabilityEntity toEntity(CapabilityPlusTechnologiesModel model) {
        return new CapabilityEntity(
                model.getId(),
                model.getName(),
                model.getDescription());
    }
}
