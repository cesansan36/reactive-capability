package com.rutaaprendizajewebflux.capability.application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityInBootcampResponse {

    private Long capabilityId;
    private String name;
    private List<TechnologyInCapabilityResponse> technologies;
}
