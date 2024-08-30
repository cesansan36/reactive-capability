package com.rutaaprendizajewebflux.capability.domain.usecase;

import com.rutaaprendizajewebflux.capability.domain.exception.CapabilityAlreadyExistsException;
import com.rutaaprendizajewebflux.capability.domain.exception.ValueNotValidException;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.SoloCapabilityModel;
import com.rutaaprendizajewebflux.capability.domain.model.Technology;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ICapabilityPersistencePort;
import com.rutaaprendizajewebflux.capability.domain.ports.out.ITechnologyCommunicationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension .class)
class SaveCapabilityUseCaseTest {

    @Mock
    private ICapabilityPersistencePort capabilityPersistencePort;

    @Mock
    private ITechnologyCommunicationPort technologyCommunicationPort;

    @Mock
    private TransactionalOperator transactionalOperator;

    private SaveCapabilityUseCase saveCapabilityUseCase;

    @BeforeEach
    void setUp() {
        saveCapabilityUseCase = new SaveCapabilityUseCase(capabilityPersistencePort, technologyCommunicationPort, transactionalOperator);
    }

    @Test
    void save_SuccessfulScenario() {
        // Given
        List<Technology> technologies = List.of(new Technology(1L, "Java"), new Technology(2L, "Spring"), new Technology(3L, "Spring Boot"));
        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", technologies);

        when(capabilityPersistencePort.findByName(anyString())).thenReturn(Mono.empty());
        when(capabilityPersistencePort.save(any(Mono.class))).thenReturn(Mono.just(capability));
        when(technologyCommunicationPort.associateTechnologiesWithCapability(any(CapabilityPlusTechnologiesModel.class))).thenReturn(Mono.empty());
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Mono<CapabilityPlusTechnologiesModel> result = saveCapabilityUseCase.save(Mono.just(capability));

        // Then
        StepVerifier.create(result)
                .expectNextMatches(savedCapability -> savedCapability.getName().equals("Backend Development") &&
                        savedCapability.getTechnologies().size() == 3)
                .verifyComplete();

        verify(capabilityPersistencePort, times(1)).save(any(Mono.class));
        verify(technologyCommunicationPort, times(1)).associateTechnologiesWithCapability(any(CapabilityPlusTechnologiesModel.class));
    }

    @Test
    void save_CapabilityAlreadyExists() {
        // Given
        List<Technology> technologies = List.of(new Technology(1L, "Java"), new Technology(2L, "Spring"), new Technology(3L, "Spring Boot"));
        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", technologies);
        SoloCapabilityModel existingCapability = new SoloCapabilityModel(1L, "Backend Development", "Backend Capability");

        // Mock findByName para devolver un Mono indicando que la capacidad ya existe
        when(capabilityPersistencePort.findByName(anyString())).thenReturn(Mono.just(existingCapability));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Mono<CapabilityPlusTechnologiesModel> result = saveCapabilityUseCase.save(Mono.just(capability));

        // Then
        StepVerifier.create(result)
                .expectError(CapabilityAlreadyExistsException.class)
                .verify();

        // Verificar que el método save no se llame, ya que la capacidad ya existe
        verify(capabilityPersistencePort, never()).save(any(Mono.class));
        verify(technologyCommunicationPort, never()).associateTechnologiesWithCapability(any(CapabilityPlusTechnologiesModel.class));
    }


    @Test
    void save_AssociationWithTechnologiesFails() {
        // Given
        List<Technology> technologies = List.of(new Technology(1L, "Java"), new Technology(2L, "Spring"), new Technology(3L, "Spring Boot"));
        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", technologies);

        when(capabilityPersistencePort.findByName(anyString())).thenReturn(Mono.empty());
        when(capabilityPersistencePort.save(any(Mono.class))).thenReturn(Mono.just(capability));
        when(technologyCommunicationPort.associateTechnologiesWithCapability(any(CapabilityPlusTechnologiesModel.class))).thenReturn(Mono.error(new RuntimeException("Failed to associate technologies")));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Mono<CapabilityPlusTechnologiesModel> result = saveCapabilityUseCase.save(Mono.just(capability));

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(capabilityPersistencePort, times(1)).save(any(Mono.class));
        verify(technologyCommunicationPort, times(1)).associateTechnologiesWithCapability(any(CapabilityPlusTechnologiesModel.class));
    }

    @Test
    void save_ValidationFailsDueToInvalidTechnologies() {
        // Given
        List<Technology> invalidTechnologies = List.of();
        CapabilityPlusTechnologiesModel invalidCapability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", invalidTechnologies);

        // Cuando la validación falla en el método validate, no se llega a los pasos siguientes.
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Mono<CapabilityPlusTechnologiesModel> result = saveCapabilityUseCase.save(Mono.just(invalidCapability));

        // Then
        StepVerifier.create(result)
                .expectError(ValueNotValidException.class)
                .verify();

        verify(capabilityPersistencePort, never()).save(any(Mono.class));
        verify(technologyCommunicationPort, never()).associateTechnologiesWithCapability(any(CapabilityPlusTechnologiesModel.class));
    }
}
