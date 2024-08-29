package com.rutaaprendizajewebflux.capability.domain.usecase;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.Technology;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ISaveCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import com.rutaaprendizajewebflux.capability.domain.util.Validator;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.List;

public class SaveCapabilityUseCase implements ISaveCapabilityServicePort {

    private final ICapabilityPersistencePort capabilityPersistencePort;
    private final ITechnologyCommunicationPort technologyCommunicationPort;
    private final TransactionalOperator transactionalOperator;

    public SaveCapabilityUseCase(ICapabilityPersistencePort capabilityPersistencePort, ITechnologyCommunicationPort technologyCommunicationPort, TransactionalOperator transactionalOperator) {
        this.capabilityPersistencePort = capabilityPersistencePort;
        this.technologyCommunicationPort = technologyCommunicationPort;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<CapabilityPlusTechnologiesModel> save(Mono<CapabilityPlusTechnologiesModel> capabilityPlusTechnologiesModel) {
        return capabilityPlusTechnologiesModel
                .map(this::validate)
                .flatMap(capability -> {
                    List<Technology> technologies = capability.getTechnologies();

                    return saveCapability(capability)
                            .map(savedCapability -> {
                                savedCapability.setTechnologies(technologies);
                                return savedCapability;
                            })
                            .flatMap(this::asociateTechnologiesWithCapability);
                })
                .as(transactionalOperator::transactional);  // Ejecutas dentro de una transacción
    }

    CapabilityPlusTechnologiesModel validate(CapabilityPlusTechnologiesModel capabilityPlusTechnologiesModel) {
        return Validator.validate(capabilityPlusTechnologiesModel);
    }

    Mono<CapabilityPlusTechnologiesModel> saveCapability(CapabilityPlusTechnologiesModel capabilityPlusTechnologiesModel) {
        return capabilityPersistencePort.save(Mono.just(capabilityPlusTechnologiesModel));
    }

    Mono<CapabilityPlusTechnologiesModel> asociateTechnologiesWithCapability(CapabilityPlusTechnologiesModel capability) {
        return technologyCommunicationPort.associateTechnologiesWithCapability(capability).thenReturn(capability);
    }
}
