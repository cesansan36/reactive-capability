package com.rutaaprendizajewebflux.capability.domain.usecase;

import com.rutaaprendizajewebflux.capability.domain.exception.CapabilityNotFoundException;
import com.rutaaprendizajewebflux.capability.domain.exception.TechnologiesNotFoundException;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IReadCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.rutaaprendizajewebflux.capability.domain.util.DomainConstants.ORDER_BY_TECHNOLOGIES;
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

    private Flux<CapabilityPlusTechnologiesModel> findPaginatedByTechnologyQuantity(int page, int size, String direction) {

        Flux<CapabilityPlusTechnologiesModel> capabilitiesIdWithTechnologies = technologyCommunicationPort
                .findPaginatedCapabilityIdsByTechnologyAmount(page, size, direction)
                .switchIfEmpty(Flux.error(new TechnologiesNotFoundException(TECHNOLOGIES_NOT_FOUND_MESSAGE)));

        Flux<Long> capabilitiesIds = capabilitiesIdWithTechnologies.map(CapabilityPlusTechnologiesModel::getId);

        Flux<CapabilityPlusTechnologiesModel> capabilities = capabilityPersistencePort
                .findAllByIds(capabilitiesIds)
                .switchIfEmpty(Flux.error(new CapabilityNotFoundException(CAPABILITY_NOT_FOUND_MESSAGE)));

        return capabilitiesIdWithTechnologies
                .flatMap(capabilityWithTech -> capabilities
                        .filter(capability -> capability.getId().equals(capabilityWithTech.getId()))
                        .map(capability -> {
                                capability.setTechnologies(capabilityWithTech.getTechnologies());
                                return capability;
                            })
                );
    }

    private Flux<CapabilityPlusTechnologiesModel> findPaginatedByField(int page, int size, String sortBy, String direction) {
        return capabilityPersistencePort
                // Existing CapabilityPlusTechnologiesModel with empty technologies list
                .findAllPaginatedByField(page, size, sortBy, direction)
                .switchIfEmpty(Flux.error(new CapabilityNotFoundException(CAPABILITY_NOT_FOUND_MESSAGE)))
                // Get technologies by capability id one by one
                .flatMap(capability ->
                        technologyCommunicationPort.getTechnologiesByCapabilityId(capability.getId())
                            .flatMap(technologies -> {
                                if (technologies.getTechnologies().isEmpty()) {
                                    return Mono.error(new TechnologiesNotFoundException(TECHNOLOGIES_NOT_FOUND_MESSAGE));
                                }
                                capability.setTechnologies(technologies.getTechnologies());
                                return Mono.just(capability);
                            })
                            .switchIfEmpty(Mono.error(new TechnologiesNotFoundException(TECHNOLOGIES_NOT_FOUND_MESSAGE)))
                );
    }

    private Flux<CapabilityPlusTechnologiesModel> findPaginatedByTechnologyQuantityFunctionalStyle(int page, int size, String direction) {
        // Get all CapabilityPlusTechnologiesModel with technologies and capability ID but without name and description
        return technologyCommunicationPort.findPaginatedCapabilityIdsByTechnologyAmount(page, size, direction)
                .flatMap(capabilityWithTech ->
                        // Get CapabilityPlusTechnologiesModel (all but technologies) by capability ID
                        capabilityPersistencePort.findAllByIds(Flux.just(capabilityWithTech.getId()))
                                .filter(capability -> capability.getId().equals(capabilityWithTech.getId()))
                                // fuses data to get CapabilityPlusTechnologiesModel with all info
                                .map(capability -> {
                                    capability.setTechnologies(capabilityWithTech.getTechnologies());
                                    return capability;
                                })
                );
    }
}
