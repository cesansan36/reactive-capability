package com.rutaaprendizajewebflux.capability.application.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface IBootcampCapabilityHandler {
    Mono<ServerResponse> save(ServerRequest request);

    Mono<ServerResponse> findCapabilitiesByBootcampId(ServerRequest serverRequest);

    Mono<ServerResponse> findPaginatedBootcampByCapabilityAmount(ServerRequest serverRequest);
}
