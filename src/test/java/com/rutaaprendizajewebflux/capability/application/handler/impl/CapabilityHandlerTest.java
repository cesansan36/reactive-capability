package com.rutaaprendizajewebflux.capability.application.handler.impl;

import com.rutaaprendizajewebflux.capability.application.dto.request.SaveCapabilityPlusTechnologiesRequest;
import com.rutaaprendizajewebflux.capability.application.dto.response.SaveCapabilityPlusTechnologiesResponse;
import com.rutaaprendizajewebflux.capability.application.dto.response.TechnologyInCapabilityResponse;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.ports.in.ISaveCapabilityServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapabilityHandlerTest {

    @Mock
    private ISaveCapabilityServicePort saveCapabilityServicePort;

    @Mock
    private ICapabilityPlusTechnologiesResponseMapper responseMapper;

    @Mock
    private ICapabilityPlusTechnologiesRequestMapper requestMapper;

    @InjectMocks
    private CapabilityHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction()).build();
    }

    private RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .POST("/capabilities", handler::save)
                .build();
    }

    @Test
    void testSave() {
        List<String> technologiesNames = List.of("Tecnologia 1", "Tecnologia 2");
        SaveCapabilityPlusTechnologiesRequest saveRequest = new SaveCapabilityPlusTechnologiesRequest("Capability 1", "Description 1", technologiesNames);
        List<TechnologyInCapabilityResponse> technologies = List.of(new TechnologyInCapabilityResponse(1L, "Tecnologia 1"), new TechnologyInCapabilityResponse(2L, "Tecnologia 2"));
        SaveCapabilityPlusTechnologiesResponse response = new SaveCapabilityPlusTechnologiesResponse(1L, "Capability 1", "Description 1", technologies);

        when(requestMapper.toModel(any(SaveCapabilityPlusTechnologiesRequest.class)))
                .thenReturn(new CapabilityPlusTechnologiesModel(null, "Capability 1", "Description 1", new ArrayList<>()));
        when(saveCapabilityServicePort.save(any(Mono.class)))
                .thenReturn(Mono.just(new CapabilityPlusTechnologiesModel(1L, "Capability 1", "Description 1", new ArrayList<>())));
        when(responseMapper.toResponse(any(CapabilityPlusTechnologiesModel.class)))
                .thenReturn(response);

        webTestClient.post()
                .uri("/capabilities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(saveRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SaveCapabilityPlusTechnologiesResponse.class)
                .value(capabilityResponse -> assertThat(capabilityResponse).usingRecursiveComparison().isEqualTo(response));
    }
}