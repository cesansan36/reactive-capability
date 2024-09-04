package com.rutaaprendizajewebflux.capability.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaveCapabilityPlusTechnologiesRequest {

    private String name;
    private String description;
    private List<String> technologiesNames;

    @Override
    public String toString() {
        return "SaveCapabilityPlusTechnologiesRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", technologiesNames=" + technologiesNames +
                '}';
    }
}
