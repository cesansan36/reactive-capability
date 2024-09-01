package com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity.CapabilityEntity;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ISoloCapabilityEntityMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.repository.ICapabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapabilityPersistenceAdapter implements ICapabilityPersistencePort {

    private final ICapabilityRepository capabilityRepository;
    private final ISoloCapabilityEntityMapper soloCapabilityEntityMapper;
    private final ICapabilityPlusTechnologyEntityMapper capabilityPlusTechnologyEntityMapper;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<SoloCapabilityModel> findById(Long id) {
        return capabilityRepository
                .findById(id)
                .map(soloCapabilityEntityMapper::toSoloModel);
    }

    @Override
    public Mono<SoloCapabilityModel> findByName(String name) {
        return capabilityRepository
                .findByName(name).map(soloCapabilityEntityMapper::toSoloModel);
    }


    @Override
    public Mono<CapabilityPlusTechnologiesModel> save(Mono<CapabilityPlusTechnologiesModel> capabilityPlusTechnologiesModel) {
        return capabilityPlusTechnologiesModel
                .map(capabilityPlusTechnologyEntityMapper::toEntity)
                .flatMap(capabilityRepository::save)
                .map(capabilityPlusTechnologyEntityMapper::toModel);
    }

    @Override
    public Flux<CapabilityPlusTechnologiesModel> findAllPaginatedByField(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);

        Query query = Query.query(Criteria.empty())
                .sort(Sort.by(sortDirection, sortBy))
                .limit(size)
                .offset((long) page * size);

        return r2dbcEntityTemplate.select(query, CapabilityEntity.class)
                .map(capabilityPlusTechnologyEntityMapper::toModel);
    }

    @Override
    public Flux<CapabilityPlusTechnologiesModel> findAllByIds(Flux<Long> ids) {
        return capabilityRepository.findAllById(ids).map(capabilityPlusTechnologyEntityMapper::toModel);
    }
}
