package com.rutaaprendizajewebflux.capability.domain.model;

import java.util.List;

public class BootcampWithCapabilitiesModel {

    private Long bootcampId;
    private List<CapabilityPlusTechnologiesModel> capabilities;

    public BootcampWithCapabilitiesModel(Long bootcampId, List<CapabilityPlusTechnologiesModel> capabilities) {
        this.bootcampId = bootcampId;
        this.capabilities = capabilities;
    }

    public BootcampWithCapabilitiesModel() {
    }

    public Long getBootcampId() {
        return bootcampId;
    }

    public void setBootcampId(Long bootcampId) {
        this.bootcampId = bootcampId;
    }

    public List<CapabilityPlusTechnologiesModel> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<CapabilityPlusTechnologiesModel> capabilities) {
        this.capabilities = capabilities;
    }
}
