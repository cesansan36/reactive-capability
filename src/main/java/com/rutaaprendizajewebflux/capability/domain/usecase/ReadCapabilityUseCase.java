package com.rutaaprendizajewebflux.capability.domain.usecase;

import com.rutaaprendizajewebflux.capability.domain.exception.CapabilityNotFoundException;
import com.rutaaprendizajewebflux.capability.domain.exception.TechnologiesNotFoundException;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IReadCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.rutaaprendizajewebflux.capability.domain.util.DomainConstants.ORDER_BY_TECHNOLOGIES;
import static com.rutaaprendizajewebflux.capability.domain.util.ExceptionConstants.AT_LEAST_ONE_CAPABILITY_NOT_FOUND_MESSAGE;
import static com.rutaaprendizajewebflux.capability.domain.util.ExceptionConstants.CAPABILITY_NOT_FOUND_MESSAGE;
import static com.rutaaprendizajewebflux.capability.domain.util.ExceptionConstants.TECHNOLOGIES_NOT_FOUND_MESSAGE;

public class ReadCapabilityUseCase implements IReadCapabilityServicePort {

    private final ITechnologyCommunicationPort technologyCommunicationPort;
    private final ICapabilityPersistencePort capabilityPersistencePort;

    public ReadCapabilityUseCase(ITechnologyCommunicationPort technologyCommunicationPort, ICapabilityPersistencePort capabilityPersistencePort) {
        this.technologyCommunicationPort = technologyCommunicationPort;
        this.capabilityPersistencePort = capabilityPersistencePort;
    }

    @Override
    public Flux<CapabilityPlusTechnologiesModel> findAllPaginated(int page, int size, String sortBy, String direction) {

        if (sortBy.equals(ORDER_BY_TECHNOLOGIES)) {
            return findPaginatedByTechnologyQuantity(page, size, direction);
        }
        else {
            return findPaginatedByField(page, size, sortBy, direction);
        }
    }

    @Override
    public Flux<CapabilityPlusTechnologiesModel> findAllByNames(List<String> names) {

        return capabilityPersistencePort
                .findAllByNames(names)
                .flatMap(this::fillCapabilityWithTechnologies)
                .collectList()
                .flatMapMany(capabilities -> {
                    if (capabilities.size() != names.size()) {
                        return Mono.error(new CapabilityNotFoundException(AT_LEAST_ONE_CAPABILITY_NOT_FOUND_MESSAGE));
                    }
                    return Flux.fromIterable(capabilities);
                });
    }

    @Override
    public Flux<CapabilityPlusTechnologiesModel> findAllByIds(List<Long> ids) {
        return capabilityPersistencePort.findAllByIds(ids).flatMap(this::fillCapabilityWithTechnologies);
    }

    private Flux<CapabilityPlusTechnologiesModel> findPaginatedByTechnologyQuantity(int page, int size, String direction) {

        Flux<CapabilityPlusTechnologiesModel> capabilitiesIdWithTechnologies = technologyCommunicationPort
                .findPaginatedCapabilityIdsByTechnologyAmount(page, size, direction)
                .switchIfEmpty(Flux.error(new TechnologiesNotFoundException(TECHNOLOGIES_NOT_FOUND_MESSAGE)));

        return capabilitiesIdWithTechnologies
                .flatMap(capabilityWithTech -> capabilityPersistencePort
                        .findAllByIds(capabilitiesIdWithTechnologies.map(CapabilityPlusTechnologiesModel::getId))
                        .switchIfEmpty(Flux.error(new CapabilityNotFoundException(CAPABILITY_NOT_FOUND_MESSAGE)))
                        .filter(capability -> capability.getId().equals(capabilityWithTech.getId()))
                        .map(capability -> {
                                capability.setTechnologies(capabilityWithTech.getTechnologies());
                                return capability;
                            })
                );
    }

    private Flux<CapabilityPlusTechnologiesModel> findPaginatedByField(int page, int size, String sortBy, String direction) {
        return capabilityPersistencePort
                .findAllPaginatedByField(page, size, sortBy, direction)
                .switchIfEmpty(Flux.error(new CapabilityNotFoundException(CAPABILITY_NOT_FOUND_MESSAGE)))
                .flatMap(this::fillCapabilityWithTechnologies);
    }

    private Mono<CapabilityPlusTechnologiesModel> fillCapabilityWithTechnologies(CapabilityPlusTechnologiesModel capability) {
        return technologyCommunicationPort
                .getTechnologiesByCapabilityId(capability.getId())
                .flatMap(technologies -> {
                    if (technologies.getTechnologies().isEmpty()) {
                        return Mono.error(new TechnologiesNotFoundException(TECHNOLOGIES_NOT_FOUND_MESSAGE));
                    }
                    capability.setTechnologies(technologies.getTechnologies());
                    return Mono.just(capability);
                })
                .switchIfEmpty(Mono.error(new TechnologiesNotFoundException(TECHNOLOGIES_NOT_FOUND_MESSAGE)));
    }
}
