package com.rutaaprendizajewebflux.capability.domain.model;

public class BootcampCapabilityRelationModel {

    private Long id;
    private Long bootcampId;
    private Long capabilityId;

    public BootcampCapabilityRelationModel(Long id, Long bootcampId, Long capabilityId) {
        this.id = id;
        this.bootcampId = bootcampId;
        this.capabilityId = capabilityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBootcampId() {
        return bootcampId;
    }

    public void setBootcampId(Long bootcampId) {
        this.bootcampId = bootcampId;
    }

    public Long getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(Long capabilityId) {
        this.capabilityId = capabilityId;
    }
}
