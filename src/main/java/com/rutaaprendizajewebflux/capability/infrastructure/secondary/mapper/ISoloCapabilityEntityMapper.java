package com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper;

import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.CapabilityEntity;

public interface ISoloCapabilityEntityMapper {
    SoloCapabilityModel toSoloModel(CapabilityEntity entity);
}
