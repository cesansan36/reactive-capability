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
public class SaveCapabilityPlusTechnologiesResponse {

    private Long id;
    private String name;
    private String description;
    private List<TechnologyInCapabilityResponse> technologies;
}
