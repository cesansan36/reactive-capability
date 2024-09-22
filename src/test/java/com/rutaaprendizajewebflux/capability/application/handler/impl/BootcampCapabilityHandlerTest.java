package com.rutaaprendizajewebflux.capability.application.handler.impl;

import com.rutaaprendizajewebflux.capability.application.dto.request.LinkBootcampWithCapabilitiesRequest;
import com.rutaaprendizajewebflux.capability.application.dto.response.BootcampWithCapabilitiesResponse;
import com.rutaaprendizajewebflux.capability.application.dto.response.CapabilityInBootcampResponse;
import com.rutaaprendizajewebflux.capability.application.mapper.IBootcampCapabilityRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.IBootcampCapabilityResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.model.BootcampWithCapabilitiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IBootcampCapabilityServicePort;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.rutaaprendizajewebflux.capability.util.Constants.GET_BY_BOOTCAMP_ID_SUB_PATH;
import static com.rutaaprendizajewebflux.capability.util.Constants.LINKED_BOOTCAMP_CAPABILITIES_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampCapabilityHandlerTest {

    @Mock
    private IBootcampCapabilityServicePort bootcampCapabilityServicePort;
    @Mock
    private IBootcampCapabilityRequestMapper bootcampCapabilityRequestMapper;
    @Mock
    private IBootcampCapabilityResponseMapper bootcampCapabilityResponseMapper;

    @InjectMocks
    private BootcampCapabilityHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction()).build();
    }

    private RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .POST(LINKED_BOOTCAMP_CAPABILITIES_PATH, handler::save)
                .GET(LINKED_BOOTCAMP_CAPABILITIES_PATH + GET_BY_BOOTCAMP_ID_SUB_PATH, handler::findCapabilitiesByBootcampId)
                .GET(LINKED_BOOTCAMP_CAPABILITIES_PATH, handler::findPaginatedBootcampByCapabilityAmount)
                .build();
    }

    @Test
    void testSave() {
        LinkBootcampWithCapabilitiesRequest request = new LinkBootcampWithCapabilitiesRequest(1L, List.of("c1", "c2"));
        BootcampWithCapabilitiesModel model = new BootcampWithCapabilitiesModel(
                1L,
                List.of(
                        new CapabilityPlusTechnologiesModel(1L, "c1", "", new ArrayList<>()),
                        new CapabilityPlusTechnologiesModel(2L, "c2", "", new ArrayList<>())));
        BootcampWithCapabilitiesResponse response = new BootcampWithCapabilitiesResponse(
                1L,
                List.of(
                        new CapabilityInBootcampResponse(1L, "c1", new ArrayList<>()),
                        new CapabilityInBootcampResponse(2L, "c2", new ArrayList<>())));

        when(bootcampCapabilityServicePort.saveAll(any())).thenReturn(Mono.just(model));
        when(bootcampCapabilityResponseMapper.toBootcampWithCapabilitiesResponse(any(BootcampWithCapabilitiesModel.class))).thenReturn(response);

        webTestClient.post()
                .uri(LINKED_BOOTCAMP_CAPABILITIES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BootcampWithCapabilitiesResponse.class)
                .value(savedResponse -> assertThat(savedResponse).usingRecursiveComparison().isEqualTo(response));
    }

    @Test
    void testFindCapabilitiesByBootcampId() {
        Long bootcampId = 1L;
        BootcampWithCapabilitiesModel model = new BootcampWithCapabilitiesModel(1L, new ArrayList<>());
        BootcampWithCapabilitiesResponse response = new BootcampWithCapabilitiesResponse(1L, new ArrayList<>());

        when(bootcampCapabilityServicePort.findAllByBootcampId(bootcampId))
                .thenReturn(Mono.just(model));
        when(bootcampCapabilityResponseMapper.toBootcampWithCapabilitiesResponse(any()))
                .thenReturn(response);

        webTestClient.get()
                .uri(LINKED_BOOTCAMP_CAPABILITIES_PATH + "/" + bootcampId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BootcampWithCapabilitiesResponse.class)
                .value(foundResponse -> assertThat(foundResponse).usingRecursiveComparison().isEqualTo(response));
    }

    @Test
    void testFindPaginatedBootcampByCapabilityAmount_DefaultPagination() {
        List<BootcampWithCapabilitiesModel> bootcampModelList = List.of(new BootcampWithCapabilitiesModel(), new BootcampWithCapabilitiesModel());
        List<BootcampWithCapabilitiesResponse> responseList = List.of(
                new BootcampWithCapabilitiesResponse(1L, List.of(new CapabilityInBootcampResponse(1L, "c1", new ArrayList<>()))),
                new BootcampWithCapabilitiesResponse(2L, List.of(new CapabilityInBootcampResponse(2L, "c2", new ArrayList<>()))));

        when(bootcampCapabilityServicePort.findAllByCapabilityAmount(0, 3, "ASC"))
                .thenReturn(Flux.fromIterable(bootcampModelList));

        when(bootcampCapabilityResponseMapper.toBootcampWithCapabilitiesResponse(any()))
                .thenReturn(responseList.get(0), responseList.get(1));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(LINKED_BOOTCAMP_CAPABILITIES_PATH)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BootcampWithCapabilitiesResponse.class)
                .value(responses -> {
                    assertThat(responses.size()).isEqualTo(2);
                    assertThat(responses.get(0)).usingRecursiveComparison().isEqualTo(responseList.get(0));
                    assertThat(responses.get(1)).usingRecursiveComparison().isEqualTo(responseList.get(1));
                });
    }

    @Test
    void testFindPaginatedBootcampByCapabilityAmount_CustomPaginationAndSorting() {
        List<BootcampWithCapabilitiesModel> bootcampModelList = List.of(new BootcampWithCapabilitiesModel(), new BootcampWithCapabilitiesModel());
        List<BootcampWithCapabilitiesResponse> responseList = List.of(
                new BootcampWithCapabilitiesResponse(1L, List.of(new CapabilityInBootcampResponse(1L, "c1", new ArrayList<>()))),
                new BootcampWithCapabilitiesResponse(2L, List.of(new CapabilityInBootcampResponse(2L, "c2", new ArrayList<>()))));

        when(bootcampCapabilityServicePort.findAllByCapabilityAmount(1, 10, "DESC"))
                .thenReturn(Flux.fromIterable(bootcampModelList));

        when(bootcampCapabilityResponseMapper.toBootcampWithCapabilitiesResponse(any()))
                .thenReturn(responseList.get(0), responseList.get(1));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(LINKED_BOOTCAMP_CAPABILITIES_PATH)
                        .queryParam("page", 1)
                        .queryParam("size", 10)
                        .queryParam("direction", "DESC")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BootcampWithCapabilitiesResponse.class)
                .value(responses -> {
                    assertThat(responses.size()).isEqualTo(2);
                    assertThat(responses.get(0)).usingRecursiveComparison().isEqualTo(responseList.get(0));
                    assertThat(responses.get(1)).usingRecursiveComparison().isEqualTo(responseList.get(1));
                });
    }
}