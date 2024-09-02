package com.rutaaprendizajewebflux.capability.application.handler.impl;

import com.rutaaprendizajewebflux.capability.application.dto.request.SaveCapabilityPlusTechnologiesRequest;
import com.rutaaprendizajewebflux.capability.application.dto.response.CapabilityPlusTechnologiesResponse;
import com.rutaaprendizajewebflux.capability.application.handler.ICapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IReadCapabilityServicePort;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ISaveCapabilityServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.rutaaprendizajewebflux.capability.domain.util.DomainConstants.ORDER_BY_DESCRIPTION;
import static com.rutaaprendizajewebflux.capability.domain.util.DomainConstants.ORDER_BY_ID;
import static com.rutaaprendizajewebflux.capability.domain.util.DomainConstants.ORDER_BY_NAME;
import static com.rutaaprendizajewebflux.capability.domain.util.DomainConstants.ORDER_BY_TECHNOLOGIES;

@RequiredArgsConstructor
@Slf4j
public class CapabilityHandler implements ICapabilityHandler {

    private final ISaveCapabilityServicePort saveCapabilityServicePort;
    private final IReadCapabilityServicePort readCapabilityServicePort;
    private final ICapabilityPlusTechnologiesResponseMapper capabilityPlusTechnologiesResponseMapper;
    private final ICapabilityPlusTechnologiesRequestMapper capabilityPlusTechnologiesRequestMapper;

    public static final List<String> ALLOWED_ORDER_BY_VALUES = List.of(ORDER_BY_ID, ORDER_BY_NAME, ORDER_BY_DESCRIPTION, ORDER_BY_TECHNOLOGIES);
    public static final List<String> ALLOWED_SORT_DIRECTIONS = List.of("ASC", "DESC");
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 3;
    public static final String DEFAULT_SORT_BY = ORDER_BY_NAME;
    public static final String DEFAULT_SORT_DIRECTION = Sort.Direction.ASC.name();

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
        int page = serverRequest.queryParam("page").map(num -> validateWholeNumber(num, DEFAULT_PAGE)).orElse(DEFAULT_PAGE);
        int size = serverRequest.queryParam("size").map(num -> validateWholeNumber(num, DEFAULT_SIZE)).orElse(DEFAULT_SIZE);
        String sortBy = serverRequest.queryParam("sortBy").filter(ALLOWED_ORDER_BY_VALUES::contains).orElse(DEFAULT_SORT_BY);
        String direction = serverRequest.queryParam("direction").filter(ALLOWED_SORT_DIRECTIONS::contains).orElse(DEFAULT_SORT_DIRECTION);

        Mono<List<CapabilityPlusTechnologiesResponse>> response = readCapabilityServicePort
                // Buscamos paginado
                .findAllPaginated(page, size, sortBy, direction)
                // Mapeamos a DTO
                .map(capabilityPlusTechnologiesResponseMapper::toResponse)
                // Ordenamos
                .collectSortedList((capability1, capability2) -> {
                    int comparisonResult;
                    if(sortBy.equalsIgnoreCase("technologies")) {
                        comparisonResult = Integer.compare(capability1.getTechnologies().size(), capability2.getTechnologies().size());
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

    int validateWholeNumber(String value, int min) {
        try {
            int number = Integer.parseInt(value);
            return Math.max(min, number);
        } catch (NumberFormatException e) {
            log.error("Error parsing number: {}", e.getMessage());
            return min;
        }
    }
}
