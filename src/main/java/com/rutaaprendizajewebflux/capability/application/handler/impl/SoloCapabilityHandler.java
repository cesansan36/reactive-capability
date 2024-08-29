package com.rutaaprendizajewebflux.capability.application.handler.impl;

import com.rutaaprendizajewebflux.capability.application.dto.response.SoloCapabilityResponse;
import com.rutaaprendizajewebflux.capability.application.handler.ISoloCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.mapper.ISoloCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ICapabilityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SoloCapabilityHandler implements ISoloCapabilityHandler {

    private final ICapabilityServicePort capabilityServicePort;
    private final ISoloCapabilityResponseMapper soloCapabilityResponseMapper;

    @Override
    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        Long id = Long.valueOf(serverRequest.pathVariable("id"));

        Mono<SoloCapabilityResponse> soloCapabilityResponse = capabilityServicePort
                .findById(id)
                .map(soloCapabilityResponseMapper::toSoloCapabilityResponse);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(soloCapabilityResponse, SoloCapabilityResponse.class);
    }
}
