package com.rutaaprendizajewebflux.capability.domain.ports.in;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import reactor.core.publisher.Flux;

public interface IReadCapabilityServicePort {

    Flux<CapabilityPlusTechnologiesModel> findAllPaginated(int page, int size, String sortBy, String direction);
}
