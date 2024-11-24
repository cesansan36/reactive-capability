package com.rutaaprendizajewebflux.capability.domain.ports.in;

import com.rutaaprendizajewebflux.capability.domain.model.BootcampWithCapabilitiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.LinkedBootcampCapabilityModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootcampCapabilityServicePort {
    Mono<BootcampWithCapabilitiesModel> saveAll(Mono<LinkedBootcampCapabilityModel> linkedModel);

    Mono<BootcampWithCapabilitiesModel> findAllByBootcampId(Long bootcampId);

    Flux<BootcampWithCapabilitiesModel> findAllByCapabilityAmount(int page, int size, String direction);
}
