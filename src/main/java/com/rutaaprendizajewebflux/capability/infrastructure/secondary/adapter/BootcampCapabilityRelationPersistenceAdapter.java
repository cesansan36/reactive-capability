package com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter;

import com.rutaaprendizajewebflux.capability.domain.model.BootcampCapabilityRelationModel;
import com.rutaaprendizajewebflux.capability.domain.ports.out.IBootcampCapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.exception.DuplicateRegistryException;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.IBootcampCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository.IBootcampCapabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.rutaaprendizajewebflux.capability.infrastructure.secondary.util.ExceptionConstants.DUPLICATE_RELATIONSHIP_EXCEPTION;

@RequiredArgsConstructor
public class BootcampCapabilityRelationPersistenceAdapter implements IBootcampCapabilityPersistencePort {

    private final IBootcampCapabilityRepository bootcampCapabilityRepository;
    private final IBootcampCapabilityEntityMapper bootcampCapabilityEntityMapper;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Flux<BootcampCapabilityRelationModel> saveAll(Flux<BootcampCapabilityRelationModel> bootcampCapabilityRelations) {
        return bootcampCapabilityRelations
                .map(bootcampCapabilityEntityMapper::toEntity)
                .collectList()
                .flatMapMany(bootcampCapabilityEntities ->
                        bootcampCapabilityRepository
                                .saveAll(bootcampCapabilityEntities)
                                .map(bootcampCapabilityEntityMapper::toModel)
                                .onErrorResume(DuplicateKeyException.class, (DuplicateKeyException e) ->
                                        Mono.error(new DuplicateRegistryException(DUPLICATE_RELATIONSHIP_EXCEPTION + e.getMessage())))
                );

    }

    @Override
    public Flux<BootcampCapabilityRelationModel> findAllByBootcampId(Long bootcampId) {
        return bootcampCapabilityRepository
                .findAllByBootcampId(bootcampId)
                .map(bootcampCapabilityEntityMapper::toModel);
    }

    @Override
    public Flux<Long> findPaginatedBootcampIdsByCapabilityAmount(int page, int size, String direction) {

        String orderByClause = "ORDER BY COUNT(capability_id) " + (direction.equalsIgnoreCase("desc") ? "DESC" : "ASC");

        String query = "SELECT bootcamp_id FROM bootcamp_capability " +
                "GROUP BY bootcamp_id " +
                orderByClause + " " +
                "LIMIT :size OFFSET :offset";

        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .bind("size", size)
                .bind("offset", page * size)
                .map(row -> row.get("bootcamp_id", Long.class))
                .all();
    }
}
