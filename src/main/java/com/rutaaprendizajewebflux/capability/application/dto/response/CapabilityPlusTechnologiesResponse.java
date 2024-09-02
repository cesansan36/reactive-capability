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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapabilityPlusTechnologiesResponse that = (CapabilityPlusTechnologiesResponse) o;
        return totalTechnologies == that.totalTechnologies && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(technologies, that.technologies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, totalTechnologies, technologies);
    }
}
