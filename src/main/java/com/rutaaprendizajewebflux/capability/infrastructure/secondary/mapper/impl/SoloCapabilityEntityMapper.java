package com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.impl;

import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.CapabilityEntity;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ISoloCapabilityEntityMapper;

public class SoloCapabilityEntityMapper implements ISoloCapabilityEntityMapper {

    @Override
    public SoloCapabilityModel toSoloModel(CapabilityEntity entity) {

        System.out.println("Desde el entity mapper");

        System.out.println(entity);

        return new SoloCapabilityModel(
                entity.getId(),
                entity.getName(),
                entity.getDescription());
    }
}
