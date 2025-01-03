package com.rutaaprendizajewebflux.capability.application.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ISoloCapabilityHandler {
    Mono<ServerResponse> findById(ServerRequest serverRequest);
}
