package com.rutaaprendizajewebflux.capability.application.handler.impl;

import com.rutaaprendizajewebflux.capability.application.dto.request.SaveCapabilityPlusTechnologiesRequest;
import com.rutaaprendizajewebflux.capability.application.dto.response.SaveCapabilityPlusTechnologiesResponse;
import com.rutaaprendizajewebflux.capability.application.handler.ICapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ISaveCapabilityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapabilityHandler implements ICapabilityHandler {

    private final ISaveCapabilityServicePort saveCapabilityServicePort;
    private final ICapabilityPlusTechnologiesResponseMapper capabilityPlusTechnologiesResponseMapper;
    private final ICapabilityPlusTechnologiesRequestMapper capabilityPlusTechnologiesRequestMapper;

    @Override
    public Mono<ServerResponse> save(ServerRequest serverRequest) {

        Mono<SaveCapabilityPlusTechnologiesRequest> request = serverRequest.bodyToMono(SaveCapabilityPlusTechnologiesRequest.class);

        Mono<SaveCapabilityPlusTechnologiesResponse> response = request
                .map(capabilityPlusTechnologiesRequestMapper::toModel)
                .flatMap(capModel -> saveCapabilityServicePort.save(Mono.just(capModel)))
                .map(capabilityPlusTechnologiesResponseMapper::toResponse);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, SaveCapabilityPlusTechnologiesResponse.class);
    }
}
