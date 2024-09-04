package com.rutaaprendizajewebflux.capability.domain.model;

import java.util.List;

public class LinkedBootcampCapabilityModel {

    private Long bootcampId;
    private List<String> capabilitiesNames;

    public LinkedBootcampCapabilityModel(Long bootcampId, List<String> capabilitiesNames) {
        this.bootcampId = bootcampId;
        this.capabilitiesNames = capabilitiesNames;
    }

    public Long getBootcampId() {
        return bootcampId;
    }

    public void setBootcampId(Long bootcampId) {
        this.bootcampId = bootcampId;
    }

    public List<String> getCapabilitiesNames() {
        return capabilitiesNames;
    }

    public void setCapabilitiesNames(List<String> capabilitiesNames) {
        this.capabilitiesNames = capabilitiesNames;
    }
}
