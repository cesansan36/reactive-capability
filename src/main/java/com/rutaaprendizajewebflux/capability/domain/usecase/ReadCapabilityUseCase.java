package com.rutaaprendizajewebflux.capability.domain.usecase;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IReadCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReadCapabilityUseCase implements IReadCapabilityServicePort {

    private final ITechnologyCommunicationPort technologyCommunicationPort;
    private final ICapabilityPersistencePort capabilityPersistencePort;

    public ReadCapabilityUseCase(ITechnologyCommunicationPort technologyCommunicationPort, ICapabilityPersistencePort capabilityPersistencePort) {
        this.technologyCommunicationPort = technologyCommunicationPort;
        this.capabilityPersistencePort = capabilityPersistencePort;
    }

    @Override
    public Flux<CapabilityPlusTechnologiesModel> findAllPaginated(int page, int size, String sortBy, String direction) {

        if (sortBy.equals("technologies")) {
            return findPaginatedByTechnologyQuantity(page, size, direction);
        }
        else {
            return findPaginatedByField(page, size, sortBy, direction);
        }
    }

    private Flux<CapabilityPlusTechnologiesModel> findPaginatedByTechnologyQuantity(int page, int size, String direction) {
        return null;
    }

    private Flux<CapabilityPlusTechnologiesModel> findPaginatedByField(int page, int size, String sortBy, String direction) {
        return capabilityPersistencePort.findAllPaginatedByField(page, size, sortBy, direction)
                .flatMap(capability -> {
                    return technologyCommunicationPort.getTechnologiesByCapabilityId(capability.getId())
                            .map(technologies -> {
                                capability.setTechnologies(technologies.getTechnologies());
                                return capability;
                            })
                            .onErrorResume(Exception.class, ex -> {
                                return Mono.error(ex);
                                // Handle technology retrieval errors (e.g., log, retry)
//                                return Mono.just(capability); // Or return an empty list of technologies
                            });
                });
    }
}
