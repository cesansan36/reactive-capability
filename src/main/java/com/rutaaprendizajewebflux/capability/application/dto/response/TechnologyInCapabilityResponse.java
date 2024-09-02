package com.rutaaprendizajewebflux.capability.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TechnologyInCapabilityResponse {

    private Long id;
    private String name;
}
