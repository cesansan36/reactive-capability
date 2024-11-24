package com.rutaaprendizajewebflux.capability.application.handler.impl;

import com.rutaaprendizajewebflux.capability.application.dto.request.LinkBootcampWithCapabilitiesRequest;
import com.rutaaprendizajewebflux.capability.application.dto.response.BootcampWithCapabilitiesResponse;
import com.rutaaprendizajewebflux.capability.application.handler.IBootcampCapabilityHandler;
import com.rutaaprendizajewebflux.capability.application.mapper.IBootcampCapabilityRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.IBootcampCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IBootcampCapabilityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.rutaaprendizajewebflux.capability.util.Validator.validateWholeNumber;

@RequiredArgsConstructor
public class BootcampCapabilityHandler implements IBootcampCapabilityHandler {

    private final IBootcampCapabilityServicePort bootcampCapabilityServicePort;
    private final IBootcampCapabilityRequestMapper bootcampCapabilityRequestMapper;
    private final IBootcampCapabilityResponseMapper bootcampCapabilityResponseMapper;

    public static final List<String> ALLOWED_SORT_DIRECTIONS = List.of("ASC", "DESC");
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 3;
    public static final String DEFAULT_SORT_DIRECTION = Sort.Direction.ASC.name();

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {

        Mono<BootcampWithCapabilitiesResponse> responseMono = request
                .bodyToMono(LinkBootcampWithCapabilitiesRequest.class)
                .map(bootcampCapabilityRequestMapper::toBootcampModel)
                .as(bootcampCapabilityServicePort::saveAll)
                .map(bootcampCapabilityResponseMapper::toBootcampWithCapabilitiesResponse);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(responseMono, BootcampWithCapabilitiesResponse.class);
    }

    @Override
    public Mono<ServerResponse> findCapabilitiesByBootcampId(ServerRequest serverRequest) {
        Long bootcampId = Long.valueOf(serverRequest.pathVariable("bootcampId"));
        Mono<BootcampWithCapabilitiesResponse> response = bootcampCapabilityServicePort
                .findAllByBootcampId(bootcampId)
                .map(bootcampCapabilityResponseMapper::toBootcampWithCapabilitiesResponse);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, BootcampWithCapabilitiesResponse.class);
    }

    @Override
    public Mono<ServerResponse> findPaginatedBootcampByCapabilityAmount(ServerRequest serverRequest) {
        int page = serverRequest.queryParam("page").map(num -> validateWholeNumber(num, DEFAULT_PAGE)).orElse(DEFAULT_PAGE);
        int size = serverRequest.queryParam("size").map(num -> validateWholeNumber(num, DEFAULT_SIZE)).orElse(DEFAULT_SIZE);
        String direction = serverRequest.queryParam("direction").filter(ALLOWED_SORT_DIRECTIONS::contains).orElse(DEFAULT_SORT_DIRECTION);

        Flux<BootcampWithCapabilitiesResponse> response = bootcampCapabilityServicePort
                .findAllByCapabilityAmount(page, size, direction)
                .map(bootcampCapabilityResponseMapper::toBootcampWithCapabilitiesResponse);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, BootcampWithCapabilitiesResponse.class);
    }
}
