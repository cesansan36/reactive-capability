package com.rutaaprendizajewebflux.capability.infrastructure.secondary.webclientobjects.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CapabilityWithTechnologyResponse {

    private Long id;
    private List<TechnologyResponse> technologies;
}
