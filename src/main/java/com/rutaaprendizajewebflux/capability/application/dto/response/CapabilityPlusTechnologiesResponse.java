package com.rutaaprendizajewebflux.capability.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "CapabilityPlusTechnologiesResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", totalTechnologies=" + totalTechnologies +
                ", technologies=" + technologies +
                '}';
    }
}
