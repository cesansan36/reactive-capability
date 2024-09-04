package com.rutaaprendizajewebflux.capability.application.handler.impl;

import com.rutaaprendizajewebflux.capability.application.dto.request.LinkBootcampWithCapabilitiesRequest;
import com.rutaaprendizajewebflux.capability.application.dto.response.BootcampWithCapabilitiesResponse;
import com.rutaaprendizajewebflux.capability.application.handler.IBootcampCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.mapper.IBootcampCapabilityRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.IBootcampCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IBootcampCapabilityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BootcampCapabilityHandler implements IBootcampCapabilityHandler {

    private final IBootcampCapabilityServicePort bootcampCapabilityServicePort;
    private final IBootcampCapabilityRequestMapper bootcampCapabilityRequestMapper;
    private final IBootcampCapabilityResponseMapper bootcampCapabilityResponseMapper;

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {

        Mono<BootcampWithCapabilitiesResponse> responseMono = request
                .bodyToMono(LinkBootcampWithCapabilitiesRequest.class)
                .map(bootcampCapabilityRequestMapper::toBootcampModel)
                .as(bootcampCapabilityServicePort::saveAll)
                .map(bootcampCapabilityResponseMapper::toBootcampWithCapabilitiesResponse);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(responseMono, BootcampWithCapabilitiesResponse.class);
    }
}
