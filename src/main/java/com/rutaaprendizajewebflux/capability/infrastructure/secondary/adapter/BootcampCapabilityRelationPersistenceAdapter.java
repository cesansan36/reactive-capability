package com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter;

import com.rutaaprendizajewebflux.capability.domain.model.BootcampCapabilityRelationModel;
import com.rutaaprendizajewebflux.capability.domain.ports.out.IBootcampCapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.exception.DuplicateRegistryException;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.IBootcampCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository.IBootcampCapabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.rutaaprendizajewebflux.capability.infrastructure.secondary.util.ExceptionConstants.DUPLICATE_RELATIONSHIP_EXCEPTION;

@RequiredArgsConstructor
public class BootcampCapabilityRelationPersistenceAdapter implements IBootcampCapabilityPersistencePort {

    private final IBootcampCapabilityRepository bootcampCapabilityRepository;
    private final IBootcampCapabilityEntityMapper bootcampCapabilityEntityMapper;

    @Override
    public Flux<BootcampCapabilityRelationModel> saveAll(Flux<BootcampCapabilityRelationModel> bootcampCapabilityRelations) {
        return bootcampCapabilityRelations
                .map(bootcampCapabilityEntityMapper::toEntity)
                .collectList()
                .flatMapMany((bootcampCapabilityEntities) ->
                        bootcampCapabilityRepository
                                .saveAll(bootcampCapabilityEntities)
                                .map(bootcampCapabilityEntityMapper::toModel)
                                .onErrorResume(DuplicateKeyException.class, (DuplicateKeyException e) ->
                                        Mono.error(new DuplicateRegistryException(DUPLICATE_RELATIONSHIP_EXCEPTION + e.getMessage())))
                );

    }
}
