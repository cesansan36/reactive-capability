package com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.exception.LinkingProcessException;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyWebclientMapper;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.webclientobjects.response.CapabilityWithTechnologyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TechnologyWebClientAdapter implements ITechnologyCommunicationPort {

    private final WebClient webClient;
    private final ICapabilityPlusTechnologyWebclientMapper capabilityPlusTechnologyWebclientMapper;

    @Override
    public Mono<CapabilityPlusTechnologiesModel> associateTechnologiesWithCapability(CapabilityPlusTechnologiesModel capability)
    {
        return webClient
                .post()
                .uri("/linked-capability-technologies")
                .bodyValue(capabilityPlusTechnologyWebclientMapper.toRequest(capability))
                .retrieve()
                .onStatus( status -> status.is4xxClientError() || status.is5xxServerError() ,
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new LinkingProcessException("No fue posible asociar las tecnologías con la capacidad: " + error))))
                .bodyToMono(CapabilityWithTechnologyResponse.class)
                .map(capabilityPlusTechnologyWebclientMapper::toModel)
                .onErrorResume(error -> Mono.error(new LinkingProcessException(error.getMessage())));
    }

    @Override
    public Mono<CapabilityPlusTechnologiesModel> getTechnologiesByCapabilityId(Long capabilityId) {
        return webClient
                .get()
                .uri("/linked-capability-technologies/{capabilityId}", capabilityId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new LinkingProcessException(errorBody))))
                .bodyToMono(CapabilityWithTechnologyResponse.class)
                .map(capabilityPlusTechnologyWebclientMapper::toModel)
                .onErrorResume(Exception.class, ex -> Mono.error(new LinkingProcessException(ex.getMessage())));
    }

    @Override
    public Flux<CapabilityPlusTechnologiesModel> findPaginatedCapabilityIdsByTechnologyAmount(int page, int size, String direction) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/linked-capability-technologies")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("direction", direction)
                        .build())
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new LinkingProcessException(errorBody))))
                .bodyToFlux(CapabilityWithTechnologyResponse.class)
                .map(capabilityPlusTechnologyWebclientMapper::toModel)
                .onErrorResume(Exception.class, ex -> Mono.error(new LinkingProcessException(ex.getMessage())));
    }
}
