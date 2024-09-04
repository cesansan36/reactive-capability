package com.rutaaprendizajewebflux.capability.infrastructure.secondary.webclientobjects.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TechnologyResponse {

    private Long id;
    private String name;

    @Override
    public String toString() {
        return "TechnologyResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
