package com.rutaaprendizajewebflux.capability.domain.usecase;

import com.rutaaprendizajewebflux.capability.domain.exception.CapabilityNotFoundException;
import com.rutaaprendizajewebflux.capability.domain.exception.TechnologiesNotFoundException;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.Technology;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static com.rutaaprendizajewebflux.capability.domain.util.DomainConstants.ORDER_BY_TECHNOLOGIES;
import static com.rutaaprendizajewebflux.capability.domain.util.ExceptionConstants.AT_LEAST_ONE_CAPABILITY_NOT_FOUND_MESSAGE;
import static com.rutaaprendizajewebflux.capability.domain.util.ExceptionConstants.TECHNOLOGIES_NOT_FOUND_MESSAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadCapabilityUseCaseTest {

    @Mock
    private ICapabilityPersistencePort capabilityPersistencePort;

    @Mock
    private ITechnologyCommunicationPort technologyCommunicationPort;

    private ReadCapabilityUseCase readCapabilityUseCase;

    @BeforeEach
    void setUp() {
        readCapabilityUseCase = new ReadCapabilityUseCase(technologyCommunicationPort, capabilityPersistencePort);
    }

    @Test
    void findAllPaginated_WithTechnologies_Success() {
        // Given
        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", List.of());

        when(technologyCommunicationPort.findPaginatedCapabilityIdsByTechnologyAmount(anyInt(), anyInt(), anyString()))
                .thenReturn(Flux.just(capability));
        when(capabilityPersistencePort.findAllByIds(any(Flux.class)))
                .thenReturn(Flux.just(capability));

        // When
        Flux<CapabilityPlusTechnologiesModel> result = readCapabilityUseCase.findAllPaginated(0, 10, ORDER_BY_TECHNOLOGIES, "asc");

        // Then
        StepVerifier.create(result)
                .expectNextMatches(capa -> capa.getName().equals("Backend Development"))
                .verifyComplete();
    }

    @Test
    void findAllPaginated_WithTechnologies_TechnologiesNotFoundException() {
        // Given
        when(technologyCommunicationPort.findPaginatedCapabilityIdsByTechnologyAmount(anyInt(), anyInt(), anyString()))
                .thenReturn(Flux.empty());

        when(capabilityPersistencePort.findAllByIds(any(Flux.class))).thenReturn(Flux.empty());

        // When
        Flux<CapabilityPlusTechnologiesModel> result = readCapabilityUseCase.findAllPaginated(0, 10, ORDER_BY_TECHNOLOGIES, "asc");

        // Then
        StepVerifier.create(result)
                .expectError(TechnologiesNotFoundException.class)
                .verify();
    }

    @Test
    void findAllPaginated_ByField_Success() {
        // Given
        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", List.of());

        when(capabilityPersistencePort.findAllPaginatedByField(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Flux.just(capability));
        when(technologyCommunicationPort.getTechnologiesByCapabilityId(anyLong()))
                .thenReturn(Mono.just(new CapabilityPlusTechnologiesModel(1L, null, null, List.of(new Technology(1L, "Java")))));

        // When
        Flux<CapabilityPlusTechnologiesModel> result = readCapabilityUseCase.findAllPaginated(0, 10, "name", "asc");

        // Then
        StepVerifier.create(result)
                .expectNextMatches(capa -> capa.getTechnologies().size() == 1)
                .verifyComplete();
    }

    @Test
    void findAllPaginated_ByField_CapabilityNotFoundException() {
        // Given
        when(capabilityPersistencePort.findAllPaginatedByField(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Flux.empty());

        // When
        Flux<CapabilityPlusTechnologiesModel> result = readCapabilityUseCase.findAllPaginated(0, 10, "name", "asc");

        // Then
        StepVerifier.create(result)
                .expectError(CapabilityNotFoundException.class)
                .verify();
    }

    @Test
    void findPaginatedByTechnologyQuantity_CapabilityNotFoundException() {
        // Given
        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", List.of());

        when(technologyCommunicationPort.findPaginatedCapabilityIdsByTechnologyAmount(anyInt(), anyInt(), anyString()))
                .thenReturn(Flux.just(capability));
        when(capabilityPersistencePort.findAllByIds(any(Flux.class)))
                .thenReturn(Flux.empty());

        // When
        Flux<CapabilityPlusTechnologiesModel> result = readCapabilityUseCase.findAllPaginated(0, 10, ORDER_BY_TECHNOLOGIES, "asc");

        // Then
        StepVerifier.create(result)
                .expectError(CapabilityNotFoundException.class)
                .verify();
    }

    @Test
    void findAllByNames_Success() {
        // Given
        List<String> names = List.of("Backend Development", "Frontend Development");

        CapabilityPlusTechnologiesModel capability1 = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", List.of());
        CapabilityPlusTechnologiesModel capability2 = new CapabilityPlusTechnologiesModel(2L, "Frontend Development", "Frontend Capability", List.of());

        Technology tech1 = new Technology(1L, "Java");
        Technology tech2 = new Technology(2L, "React");

        when(capabilityPersistencePort.findAllByNames(names))
                .thenReturn(Flux.just(capability1, capability2));

        when(technologyCommunicationPort.getTechnologiesByCapabilityId(1L))
                .thenReturn(Mono.just(new CapabilityPlusTechnologiesModel(1L, null, null, List.of(tech1))));

        when(technologyCommunicationPort.getTechnologiesByCapabilityId(2L))
                .thenReturn(Mono.just(new CapabilityPlusTechnologiesModel(2L, null, null, List.of(tech2))));

        // When
        Flux<CapabilityPlusTechnologiesModel> result = readCapabilityUseCase.findAllByNames(names);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(capa ->
                        capa.getId().equals(1L) &&
                                capa.getName().equals("Backend Development") &&
                                capa.getTechnologies().size() == 1 &&
                                capa.getTechnologies().get(0).getName().equals("Java")
                )
                .expectNextMatches(capa ->
                        capa.getId().equals(2L) &&
                                capa.getName().equals("Frontend Development") &&
                                capa.getTechnologies().size() == 1 &&
                                capa.getTechnologies().get(0).getName().equals("React")
                )
                .verifyComplete();
    }

    @Test
    void findAllByNames_CapabilityNotFoundException() {
        List<String> names = List.of("Frontend Development", "Backend Development");

        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(
                1L,
                "Frontend Development",
                "Frontend Capability",
                List.of(new Technology(1L, "Angular"), new Technology(2L, "React")));
        Flux<CapabilityPlusTechnologiesModel> capabilitiesFoundFlux = Flux.just(capability);
        CapabilityPlusTechnologiesModel capabilityWithTechnologyData = new CapabilityPlusTechnologiesModel(
                1L,
                null,
                null,
                List.of(new Technology(1L, "Angular"), new Technology(2L, "React")));
        Mono<CapabilityPlusTechnologiesModel> capabilityWithTechnologyDataMono = Mono.just(capabilityWithTechnologyData);

        when(capabilityPersistencePort.findAllByNames(names)).thenReturn(capabilitiesFoundFlux);
        when(technologyCommunicationPort.getTechnologiesByCapabilityId(anyLong())).thenReturn(capabilityWithTechnologyDataMono);

        Flux<CapabilityPlusTechnologiesModel> result = readCapabilityUseCase.findAllByNames(names);

        StepVerifier.create(result)
                .expectError(CapabilityNotFoundException.class)
                .verify();
    }

    @Test
    void findAllByNames_TechnologiesNotFoundException() {
        // Given
        List<String> names = List.of("Backend Development");

        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", List.of());

        when(capabilityPersistencePort.findAllByNames(names))
                .thenReturn(Flux.just(capability));

        when(technologyCommunicationPort.getTechnologiesByCapabilityId(1L))
                .thenReturn(Mono.just(new CapabilityPlusTechnologiesModel(1L, null, null, List.of()))); // Tecnologías vacías

        // When
        Flux<CapabilityPlusTechnologiesModel> result = readCapabilityUseCase.findAllByNames(names);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof TechnologiesNotFoundException &&
                                throwable.getMessage().equals(TECHNOLOGIES_NOT_FOUND_MESSAGE)
                )
                .verify();
    }

    @Test
    void findAllByNames_EmptyNamesList() {
        // Given
        List<String> names = List.of();

        when(capabilityPersistencePort.findAllByNames(names))
                .thenReturn(Flux.empty());

        // When
        Flux<CapabilityPlusTechnologiesModel> result = readCapabilityUseCase.findAllByNames(names);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findAllByIds_ShouldReturnCapabilitiesWithTechnologies() {
        // Datos de prueba
        List<Long> ids = Arrays.asList(1L, 2L);

        CapabilityPlusTechnologiesModel capability1 = new CapabilityPlusTechnologiesModel(1L, "Capability 1", "Description 1", null);
        CapabilityPlusTechnologiesModel capability2 = new CapabilityPlusTechnologiesModel(2L, "Capability 2", "Description 2", null);
        Flux<CapabilityPlusTechnologiesModel> capabilitiesWithNoTechnologies = Flux.just(capability1, capability2);

        List<Technology> technologiesForCapability1 = Arrays.asList(new Technology(1L, "Tech 1"), new Technology(2L, "Tech 2"));
        List<Technology> technologiesForCapability2 = Arrays.asList(new Technology(3L, "Tech 3"));

        CapabilityPlusTechnologiesModel capability1WithTech = new CapabilityPlusTechnologiesModel(1L, null, null, technologiesForCapability1);
        CapabilityPlusTechnologiesModel capability2WithTech = new CapabilityPlusTechnologiesModel(2L, null, null, technologiesForCapability2);

        // Mock del puerto de persistencia
        when(capabilityPersistencePort.findAllByIds(ids)).thenReturn(capabilitiesWithNoTechnologies);
        // Mock del puerto de comunicación
        when(technologyCommunicationPort.getTechnologiesByCapabilityId(1L))
                .thenReturn(Mono.just(capability1WithTech));
        when(technologyCommunicationPort.getTechnologiesByCapabilityId(2L))
                .thenReturn(Mono.just(capability2WithTech));

        // Ejecución y verificación
        StepVerifier.create(readCapabilityUseCase.findAllByIds(ids))
                .expectNextMatches(capability -> capability.getId().equals(1L) && capability.getTechnologies().size() == 2)
                .expectNextMatches(capability -> capability.getId().equals(2L) && capability.getTechnologies().size() == 1)
                .verifyComplete();
    }

    @Test
    void findAllByIds_ShouldThrowExceptionWhenTechnologiesNotFound() {
        // Datos de prueba
        List<Long> ids = Arrays.asList(1L);

        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Capability 1", "Description 1", null);

        // Mock del puerto de persistencia
        when(capabilityPersistencePort.findAllByIds(ids)).thenReturn(Flux.just(capability));

        // Mock del puerto de comunicación para lanzar la excepción
        when(technologyCommunicationPort.getTechnologiesByCapabilityId(1L))
                .thenReturn(Mono.empty()); // Lista vacía

        // Ejecución y verificación
        StepVerifier.create(readCapabilityUseCase.findAllByIds(ids))
                .expectError(TechnologiesNotFoundException.class)
                .verify();
    }
}