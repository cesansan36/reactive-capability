package com.rutaaprendizajewebflux.capability.domain.usecase;

import com.rutaaprendizajewebflux.capability.domain.model.BootcampCapabilityRelationModel;
import com.rutaaprendizajewebflux.capability.domain.model.BootcampWithCapabilitiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.LinkedBootcampCapabilityModel;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IBootcampCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IReadCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.IBootcampCapabilityPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class BootcampCapabilityUseCase implements IBootcampCapabilityServicePort {

    private final IReadCapabilityServicePort capabilityServicePort;
    private final IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort;

    public BootcampCapabilityUseCase(IReadCapabilityServicePort capabilityServicePort, IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort) {
        this.capabilityServicePort = capabilityServicePort;
        this.bootcampCapabilityPersistencePort = bootcampCapabilityPersistencePort;
    }

    @Override
    public Mono<BootcampWithCapabilitiesModel> saveAll(Mono<LinkedBootcampCapabilityModel> linkedModel) {
        return linkedModel
                .flatMapMany(link -> {

                    Long bootcampId = link.getBootcampId();

                    Flux<CapabilityPlusTechnologiesModel> capabilities  = Mono.just(link.getCapabilitiesNames())
                            .flatMapMany(capabilityServicePort::findAllByNames);

                    return capabilities
                            .map(capability -> new BootcampCapabilityRelationModel(null, bootcampId, capability.getId()))
                            .as(bootcampCapabilityPersistencePort::saveAll)
                            .thenMany(capabilities.collectList().map(list -> buildBootcampWithCapabilities(bootcampId, list)));
                })
                .next();
    }

    @Override
    public Mono<BootcampWithCapabilitiesModel> findAllByBootcampId(Long bootcampId) {
        return bootcampCapabilityPersistencePort
                .findAllByBootcampId(bootcampId)
                .map(BootcampCapabilityRelationModel::getCapabilityId)
                .collectList()
                .flatMapMany(capabilityServicePort::findAllByIds)
                .collectList()
                .map(capabilityList -> {
                    BootcampWithCapabilitiesModel bootcampWithCapabilities = new BootcampWithCapabilitiesModel();
                    bootcampWithCapabilities.setBootcampId(bootcampId);
                    bootcampWithCapabilities.setCapabilities(capabilityList);
                    return bootcampWithCapabilities;
                });
    }

    @Override
    public Flux<BootcampWithCapabilitiesModel> findAllByCapabilityAmount(int page, int size, String direction) {
        return bootcampCapabilityPersistencePort
                .findPaginatedBootcampIdsByCapabilityAmount(page, size, direction)
                .flatMap(this::findAllByBootcampId);
    }

    private BootcampWithCapabilitiesModel buildBootcampWithCapabilities(Long bootcampId, List<CapabilityPlusTechnologiesModel> capabilities) {
        return new BootcampWithCapabilitiesModel(bootcampId, capabilities);
    }
}
