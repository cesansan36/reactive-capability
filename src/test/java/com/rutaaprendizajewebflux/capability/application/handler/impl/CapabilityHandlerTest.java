package com.rutaaprendizajewebflux.capability.application.handler.impl;

import com.rutaaprendizajewebflux.capability.application.dto.request.SaveCapabilityPlusTechnologiesRequest;
import com.rutaaprendizajewebflux.capability.application.dto.response.CapabilityPlusTechnologiesResponse;
import com.rutaaprendizajewebflux.capability.application.dto.response.TechnologyInCapabilityResponse;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesRequestMapper;
import com.rutaaprendizajewebflux.capability.application.mapper.ICapabilityPlusTechnologiesResponseMapper;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.ports.in.IReadCapabilityServicePort;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.rutaaprendizajewebflux.capability.application.handler.impl.CapabilityHandler.DEFAULT_PAGE;
import static com.rutaaprendizajewebflux.capability.application.handler.impl.CapabilityHandler.DEFAULT_SIZE;
import static com.rutaaprendizajewebflux.capability.application.handler.impl.CapabilityHandler.DEFAULT_SORT_BY;
import static com.rutaaprendizajewebflux.capability.application.handler.impl.CapabilityHandler.DEFAULT_SORT_DIRECTION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapabilityHandlerTest {

    @Mock
    private ISaveCapabilityServicePort saveCapabilityServicePort;

    @Mock
    private IReadCapabilityServicePort readCapabilityServicePort;

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
                .GET("/capabilities", handler::findAllPaginated)
                .build();
    }

    @Test
    void testSave() {
        List<String> technologiesNames = List.of("Tecnologia 1", "Tecnologia 2");
        SaveCapabilityPlusTechnologiesRequest saveRequest = new SaveCapabilityPlusTechnologiesRequest("Capability 1", "Description 1", technologiesNames);
        List<TechnologyInCapabilityResponse> technologies = List.of(new TechnologyInCapabilityResponse(1L, "Tecnologia 1"), new TechnologyInCapabilityResponse(2L, "Tecnologia 2"));
        CapabilityPlusTechnologiesResponse response = new CapabilityPlusTechnologiesResponse(1L, "Capability 1", "Description 1", 2, technologies);

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
                .expectBody(CapabilityPlusTechnologiesResponse.class)
                .value(capabilityResponse -> assertThat(capabilityResponse).usingRecursiveComparison().isEqualTo(response));
    }

    @Test
    void testFindAllPaginated_DefaultPagination() {
        CapabilityPlusTechnologiesModel capabilityModel1 = new CapabilityPlusTechnologiesModel(1L, "Capability 1", "Description 1", new ArrayList<>());
        CapabilityPlusTechnologiesModel capabilityModel2 = new CapabilityPlusTechnologiesModel(2L, "Capability 2", "Description 2", new ArrayList<>());
        Flux<CapabilityPlusTechnologiesModel> capabilityModels = Flux.just(capabilityModel1, capabilityModel2);

        when(readCapabilityServicePort.findAllPaginated(0, 5, "name", "ASC"))
                .thenReturn(capabilityModels);

        when(responseMapper.toResponse(any(CapabilityPlusTechnologiesModel.class)))
                .thenAnswer(invocation -> {
                    CapabilityPlusTechnologiesModel model = invocation.getArgument(0);
                    return new CapabilityPlusTechnologiesResponse(model.getId(), model.getName(), model.getDescription(), 0, new ArrayList<>());
                });

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/capabilities")
                        .queryParam("page", 0)
                        .queryParam("size", 5)
                        .queryParam("sortBy", "name")
                        .queryParam("direction", "ASC")
                        .build())

                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CapabilityPlusTechnologiesResponse.class)
                .value(capabilities -> {
                    assertThat(capabilities.size()).isEqualTo(2);
                    assertThat(capabilities.get(0).getName()).isEqualTo("Capability 1");
                    assertThat(capabilities.get(1).getName()).isEqualTo("Capability 2");
                });
    }

    @Test
    void testFindAllPaginated_CustomPaginationAndSorting() {
        // Arrange
        CapabilityPlusTechnologiesModel model1 = new CapabilityPlusTechnologiesModel(1L, "Capability A", "Description A", new ArrayList<>());
        CapabilityPlusTechnologiesModel model2 = new CapabilityPlusTechnologiesModel(2L, "Capability B", "Description B", new ArrayList<>());
        CapabilityPlusTechnologiesResponse response1 = new CapabilityPlusTechnologiesResponse(1L, "Capability A", "Description A", 0, new ArrayList<>());
        CapabilityPlusTechnologiesResponse response2 = new CapabilityPlusTechnologiesResponse(2L, "Capability B", "Description B", 0, new ArrayList<>());

        when(readCapabilityServicePort.findAllPaginated(1, 10, "id", "DESC"))
                .thenReturn(Flux.just(model2, model1));
        when(responseMapper.toResponse(model1))
                .thenReturn(response1);
        when(responseMapper.toResponse(model2))
                .thenReturn(response2);

        // Act & Assert
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/capabilities")
                        .queryParam("page", "1")
                        .queryParam("size", "10")
                        .queryParam("sortBy", "id")
                        .queryParam("direction", "DESC")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CapabilityPlusTechnologiesResponse.class)
                .value(responses -> {
                    List<CapabilityPlusTechnologiesResponse> responsesList = new ArrayList<>(responses);
                    assertThat(responsesList.get(0)).isEqualTo(response2);
                    assertThat(responsesList.get(1)).isEqualTo(response1);
                });
    }

    @Test
    void testFindAllPaginated_SortByTechnologies() {
        // Arrange
        String sortBy = "technologies";
        String direction = "ASC";
        List<TechnologyInCapabilityResponse> technologies1 = List.of(new TechnologyInCapabilityResponse(1L, "Tech 1"));
        List<TechnologyInCapabilityResponse> technologies2 = List.of(new TechnologyInCapabilityResponse(2L, "Tech 2"), new TechnologyInCapabilityResponse(3L, "Tech 3"));

        CapabilityPlusTechnologiesModel model1 = new CapabilityPlusTechnologiesModel(1L, "Capability A", "Description A", new ArrayList<>());
        CapabilityPlusTechnologiesModel model2 = new CapabilityPlusTechnologiesModel(2L, "Capability B", "Description B", new ArrayList<>());
        CapabilityPlusTechnologiesResponse response1 = new CapabilityPlusTechnologiesResponse(1L, "Capability A", "Description A", 1, technologies1);
        CapabilityPlusTechnologiesResponse response2 = new CapabilityPlusTechnologiesResponse(2L, "Capability B", "Description B", 2, technologies2);


        when(readCapabilityServicePort.findAllPaginated(DEFAULT_PAGE, DEFAULT_SIZE, sortBy, direction))
                .thenReturn(Flux.just(model1, model2));
        when(responseMapper.toResponse(model1))
                .thenReturn(response1);
        when(responseMapper.toResponse(model2))
                .thenReturn(response2);

        // Act & Assert
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/capabilities")
                        .queryParam("sortBy", sortBy)
                        .queryParam("direction", direction)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CapabilityPlusTechnologiesResponse.class)
                .value(responses -> {
                    List<CapabilityPlusTechnologiesResponse> responsesList = new ArrayList<>(responses);
                    assertThat(responsesList.get(0)).isEqualTo(response1);
                    assertThat(responsesList.get(1)).isEqualTo(response2);
                });
    }

    @Test
    void testFindAllPaginated_InvalidPaginationParams() {
        List<TechnologyInCapabilityResponse> technologies1 = List.of(new TechnologyInCapabilityResponse(1L, "Tech 1"));
        List<TechnologyInCapabilityResponse> technologies2 = List.of(new TechnologyInCapabilityResponse(2L, "Tech 2"), new TechnologyInCapabilityResponse(3L, "Tech 3"));

        CapabilityPlusTechnologiesModel model1 = new CapabilityPlusTechnologiesModel(1L, "Capability A", "Description A", new ArrayList<>());
        CapabilityPlusTechnologiesModel model2 = new CapabilityPlusTechnologiesModel(2L, "Capability B", "Description B", new ArrayList<>());
        CapabilityPlusTechnologiesResponse response1 = new CapabilityPlusTechnologiesResponse(1L, "Capability A", "Description A", 1, technologies1);
        CapabilityPlusTechnologiesResponse response2 = new CapabilityPlusTechnologiesResponse(2L, "Capability B", "Description B", 2, technologies2);

        when(readCapabilityServicePort.findAllPaginated(DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION))
                .thenReturn(Flux.just(model1, model2));
        when(responseMapper.toResponse(model1))
                .thenReturn(response1);
        when(responseMapper.toResponse(model2))
                .thenReturn(response2);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/capabilities")
                        .queryParam("page", "-1")  // Invalid page number
                        .queryParam("size", "DIEZ")   // Invalid size number
                        .queryParam("sortBy", "robotModel")
                        .queryParam("direction", "bothSides")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CapabilityPlusTechnologiesResponse.class)
                .value(responses -> {
                    List<CapabilityPlusTechnologiesResponse> responsesList = new ArrayList<>(responses);
                    assertThat(responsesList.get(0)).isEqualTo(response1);
                    assertThat(responsesList.get(1)).isEqualTo(response2);
                });
    }

    @Test
    void testFindAllPaginated_EmptyResult() {
        // Arrange
        when(readCapabilityServicePort.findAllPaginated(DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION))
                .thenReturn(Flux.empty());

        // Act & Assert
        webTestClient.get()
                .uri("/capabilities")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CapabilityPlusTechnologiesResponse.class)
                .hasSize(0);
    }
}