package com.rutaaprendizajewebflux.capability.application.handler.impl;

import com.rutaaprendizajewebflux.capability.application.dto.request.SaveCapabilityPlusTechnologiesRequest;
import com.rutaaprendizajewebflux.capability.application.dto.response.CapabilityPlusTechnologiesResponse;
import com.rutaaprendizajewebflux.capability.application.handler.ICapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IReadCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ISaveCapabilityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class CapabilityHandler implements ICapabilityHandler {

    private final ISaveCapabilityServicePort saveCapabilityServicePort;
    private final IReadCapabilityServicePort readCapabilityServicePort;
    private final ICapabilityPlusTechnologiesResponseMapper capabilityPlusTechnologiesResponseMapper;
    private final ICapabilityPlusTechnologiesRequestMapper capabilityPlusTechnologiesRequestMapper;

    @Override
    public Mono<ServerResponse> save(ServerRequest serverRequest) {

        Mono<SaveCapabilityPlusTechnologiesRequest> request = serverRequest.bodyToMono(SaveCapabilityPlusTechnologiesRequest.class);

        Mono<CapabilityPlusTechnologiesResponse> response = request
                .map(capabilityPlusTechnologiesRequestMapper::toModel)
                .flatMap(capModel -> saveCapabilityServicePort.save(Mono.just(capModel)))
                .map(capabilityPlusTechnologiesResponseMapper::toResponse);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, CapabilityPlusTechnologiesResponse.class);
    }

    @Override
    public Mono<ServerResponse> findAllPaginated(ServerRequest serverRequest) {
        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(5);
        String sortBy = serverRequest.queryParam("sortBy").orElse("name");
        String direction = serverRequest.queryParam("direction").orElse(Sort.Direction.ASC.name());

        Mono<List<CapabilityPlusTechnologiesResponse>> response = readCapabilityServicePort
                // Buscamos paginado
                .findAllPaginated(page, size, sortBy, direction)
                // Mapeamos a DTO
                .map(capabilityPlusTechnologiesResponseMapper::toResponse)
                // Ordenamos
                .collectSortedList((capability1, capability2) -> {
                    int comparisonResult;
                    if(sortBy.equalsIgnoreCase("technologies")) {
                        comparisonResult = Integer.compare(capability1.getTotalTechnologies(), capability2.getTotalTechnologies());
                    } else if (sortBy.equalsIgnoreCase("name")) {
                        comparisonResult = capability1.getName().compareToIgnoreCase(capability2.getName());
                    }
                    else {
                        comparisonResult = capability1.getId().compareTo(capability2.getId());
                    }
                    return direction.equalsIgnoreCase("ASC") ? comparisonResult : -comparisonResult;
                });

        return ServerResponse
                .ok()
                .body(response, CapabilityPlusTechnologiesResponse.class);
    }
}
