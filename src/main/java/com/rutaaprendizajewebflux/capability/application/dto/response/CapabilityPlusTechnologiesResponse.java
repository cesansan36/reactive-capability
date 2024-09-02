package com.rutaaprendizajewebflux.capability.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CapabilityPlusTechnologiesResponse {

    private Long id;
    private String name;
    private String description;
    private int totalTechnologies;
    private List<TechnologyInCapabilityResponse> technologies;
}
