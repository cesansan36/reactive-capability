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

import java.util.List;

import static com.rutaaprendizajewebflux.capability.domain.util.DomainConstants.ORDER_BY_TECHNOLOGIES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
}