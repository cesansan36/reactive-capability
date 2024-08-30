package com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter;

import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.mapper.ICapabilityPlusTechnologyWebclientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TechnologyWebClientAdapter implements ITechnologyCommunicationPort {

    private final WebClient webClient;
    private final ICapabilityPlusTechnologyWebclientMapper capabilityPlusTechnologyWebclientMapper;

    @Override
    public Mono<Void> associateTechnologiesWithCapability(CapabilityPlusTechnologiesModel capability)
    {
        return webClient
                .post()
                .uri("/linked-capability-technologies")
                .bodyValue(capabilityPlusTechnologyWebclientMapper.toRequest(capability))
                .retrieve()
                .onStatus( status -> status.is4xxClientError() || status.is5xxServerError() ,
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("No fue posible asociar las tecnologías con la capacidad: " + error))))
                .bodyToMono(Void.class)
                .onErrorResume(error -> Mono.error(new RuntimeException(error.getMessage())));
    }
}
