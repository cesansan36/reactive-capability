package com.rutaaprendizajewebflux.capability.domain.usecase;

import com.rutaaprendizajewebflux.capability.domain.model.BootcampCapabilityRelationModel;
import com.rutaaprendizajewebflux.capability.domain.model.BootcampWithCapabilitiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.LinkedBootcampCapabilityModel;
import com.rutaaprendizajewebflux.capability.infrastructure.secondary.adapter.BootcampCapabilityRelationPersistenceAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampCapabilityUseCaseTest {

    @Mock
    private ReadCapabilityUseCase readCapabilityUseCase;
    @Mock
    private BootcampCapabilityRelationPersistenceAdapter bootcampCapabilityRelationPersistenceAdapter;

    @InjectMocks
    private BootcampCapabilityUseCase bootcampCapabilityUseCase;

    @Test
    void saveAll_ShouldSaveCapabilitiesAndReturnModel() {
        // Arrange
        LinkedBootcampCapabilityModel linkedModel = new LinkedBootcampCapabilityModel(1L, List.of("Java", "Spring"));
        linkedModel.setBootcampId(1L);
        linkedModel.setCapabilitiesNames(Arrays.asList("Java", "Spring"));

        CapabilityPlusTechnologiesModel capability1 = new CapabilityPlusTechnologiesModel(1L, "Java", "Java description", List.of());
        CapabilityPlusTechnologiesModel capability2 = new CapabilityPlusTechnologiesModel(2L, "Spring", "Spring description", List.of());

        when(readCapabilityUseCase.findAllByNames(any())).thenReturn(Flux.just(capability1, capability2));
        when(bootcampCapabilityRelationPersistenceAdapter.saveAll(any())).thenReturn(Flux.empty());

        // Act
        Mono<BootcampWithCapabilitiesModel> result = bootcampCapabilityUseCase.saveAll(Mono.just(linkedModel));

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(model ->
                        model.getBootcampId().equals(1L) &&
                                model.getCapabilities().size() == 2 &&
                                model.getCapabilities().get(0).getId().equals(1L) &&
                                model.getCapabilities().get(1).getId().equals(2L))
                .verifyComplete();

        verify(readCapabilityUseCase, times(1)).findAllByNames(any());
        verify(bootcampCapabilityRelationPersistenceAdapter, times(1)).saveAll(any());
    }

    @Test
    void findAllByBootcampId_ShouldReturnBootcampWithCapabilities() {
        // Arrange
        Long bootcampId = 1L;
        BootcampCapabilityRelationModel relation1 = new BootcampCapabilityRelationModel(1L, bootcampId, 1L);
        BootcampCapabilityRelationModel relation2 = new BootcampCapabilityRelationModel(2L, bootcampId, 2L);

        CapabilityPlusTechnologiesModel capability1 = new CapabilityPlusTechnologiesModel(1L, "Java", "Java description", List.of());
        CapabilityPlusTechnologiesModel capability2 = new CapabilityPlusTechnologiesModel(2L, "Spring", "Spring description", List.of());

        when(bootcampCapabilityRelationPersistenceAdapter.findAllByBootcampId(anyLong())).thenReturn(Flux.just(relation1, relation2));
        when(readCapabilityUseCase.findAllByIds(anyList())).thenReturn(Flux.just(capability1, capability2));

        // Act
        Mono<BootcampWithCapabilitiesModel> result = bootcampCapabilityUseCase.findAllByBootcampId(bootcampId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(model ->
                        model.getBootcampId().equals(bootcampId) &&
                                model.getCapabilities().size() == 2 &&
                                model.getCapabilities().get(0).getId().equals(1L) &&
                                model.getCapabilities().get(1).getId().equals(2L))
                .verifyComplete();

        verify(bootcampCapabilityRelationPersistenceAdapter, times(1)).findAllByBootcampId(anyLong());
        verify(readCapabilityUseCase, times(1)).findAllByIds(anyList());
    }

    @Test
    void findAllByCapabilityAmount() {
        int page = 0;
        int size = 2;
        String direction = "ASC";

        List<Long> bootcampIds = List.of(1L, 2L);
        Flux<Long> bootcampIdsFlux = Flux.fromIterable(bootcampIds);

        BootcampCapabilityRelationModel relation1 = new BootcampCapabilityRelationModel(1L, 1L, 1L);
        Flux<BootcampCapabilityRelationModel> relationsFlux1 = Flux.just(relation1);
        BootcampCapabilityRelationModel relation2 = new BootcampCapabilityRelationModel(2L, 2L, 2L);
        Flux<BootcampCapabilityRelationModel> relationsFlux2 = Flux.just(relation2);

        CapabilityPlusTechnologiesModel capability1 = new CapabilityPlusTechnologiesModel(1L, "Java", "Java description", List.of());
        Flux<CapabilityPlusTechnologiesModel> capabilitiesFlux1 = Flux.just(capability1);
        CapabilityPlusTechnologiesModel capability2 = new CapabilityPlusTechnologiesModel(2L, "Spring", "Spring description", List.of());
        Flux<CapabilityPlusTechnologiesModel> capabilitiesFlux2 = Flux.just(capability2);

        when(bootcampCapabilityRelationPersistenceAdapter.findPaginatedBootcampIdsByCapabilityAmount(anyInt(), anyInt(), anyString())).thenReturn(bootcampIdsFlux);
        when(bootcampCapabilityRelationPersistenceAdapter.findAllByBootcampId(anyLong()))
                .thenAnswer(invocation -> {
                    Long bootcampId = invocation.getArgument(0);
                    if (bootcampId == 1L) {
                        return relationsFlux1;
                    } else {
                        return relationsFlux2;
                    }
                });
        when(readCapabilityUseCase.findAllByIds(anyList())).thenReturn(capabilitiesFlux1).thenReturn(capabilitiesFlux2);

        Flux<BootcampWithCapabilitiesModel> resultFlux = bootcampCapabilityUseCase.findAllByCapabilityAmount(page, size, direction);

        StepVerifier.create(resultFlux)
                .expectNextMatches(bootcamp ->
                        bootcamp.getBootcampId().equals(1L) &&
                                bootcamp.getCapabilities().size() == 1 &&
                                bootcamp.getCapabilities().get(0).getName().equals("Java"))
                .expectNextMatches(bootcamp ->
                        bootcamp.getBootcampId().equals(2L) &&
                                bootcamp.getCapabilities().size() == 1 &&
                                bootcamp.getCapabilities().get(0).getName().equals("Spring"))
                .verifyComplete();
    }
}