package com.rutaaprendizajewebflux.capability.application.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ICapabilityHandler {
    Mono<ServerResponse> save(ServerRequest serverRequest);

    Mono<ServerResponse> findAllPaginated(ServerRequest serverRequest);
}
