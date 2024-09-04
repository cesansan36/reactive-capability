package com.rutaaprendizajewebflux.capability.domain.ports.in;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IReadCapabilityServicePort {

    Flux<CapabilityPlusTechnologiesModel> findAllPaginated(int page, int size, String sortBy, String direction);

    Flux<CapabilityPlusTechnologiesModel> findAllByNames(List<String> names);
}
