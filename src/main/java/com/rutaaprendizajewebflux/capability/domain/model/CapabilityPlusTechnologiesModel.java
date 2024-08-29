package com.rutaaprendizajewebflux.capability.domain.model;

import java.util.List;

public class CapabilityPlusTechnologiesModel {

    private Long id;
    private String name;
    private String description;
    private List<Technology> technologies;

    public CapabilityPlusTechnologiesModel(Long id, String name, String description, List<Technology> technologies) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.technologies = technologies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }

    public List<String> getTechnologiesNames() {
        return technologies
                .stream()
                .map(Technology::getName)
                .toList();
    }
}
