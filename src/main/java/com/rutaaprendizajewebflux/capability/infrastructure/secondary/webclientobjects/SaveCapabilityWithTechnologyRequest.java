package com.rutaaprendizajewebflux.capability.infrastructure.secondary.webclientobjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaveCapabilityWithTechnologyRequest {

    private Long capabilityId;
    private List<String> technologiesNames;
}
