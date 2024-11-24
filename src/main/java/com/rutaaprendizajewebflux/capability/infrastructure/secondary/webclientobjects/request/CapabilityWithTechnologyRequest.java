package com.rutaaprendizajewebflux.capability.infrastructure.secondary.webclientobjects.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CapabilityWithTechnologyRequest {

    private Long capabilityId;
    private List<String> technologiesNames;
}
