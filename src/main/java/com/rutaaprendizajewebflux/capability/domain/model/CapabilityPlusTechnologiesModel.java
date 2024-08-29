package com.rutaaprendizajewebflux.capability.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void removeDuplicatedTechnologiesByName() {
        if (technologies == null || technologies.isEmpty()) {
            return; // Nothing to do if the list is null or empty
        }

        Set<String> uniqueTechnologyNames = new HashSet<>();
        List<Technology> filteredTechnologies = new ArrayList<>();

        for (Technology technology : technologies) {
            if (technology != null && technology.getName() != null && uniqueTechnologyNames.add(technology.getName())) {
                filteredTechnologies.add(technology);
            }
        }

        this.technologies = filteredTechnologies;
    }

    @Override
    public String toString() {
        return "CapabilityPlusTechnologiesModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", technologies=" + technologies +
                '}';
    }
}
