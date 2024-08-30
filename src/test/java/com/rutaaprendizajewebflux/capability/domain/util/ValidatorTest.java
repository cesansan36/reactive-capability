package com.rutaaprendizajewebflux.capability.domain.util;

import com.rutaaprendizajewebflux.capability.domain.exception.ValueNotValidException;
import com.rutaaprendizajewebflux.capability.domain.model.CapabilityPlusTechnologiesModel;
import com.rutaaprendizajewebflux.capability.domain.model.Technology;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {

    @Test
    void shouldPassValidationWithValidTechnologies() {
        // Given
        List<Technology> technologies = List.of(
                new Technology(1L, "Java"),
                new Technology(2L, "Spring"),
                new Technology(3L, "Spring Boot")
        );
        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", technologies);

        // When
        CapabilityPlusTechnologiesModel result = Validator.validate(capability);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTechnologies()).hasSize(3);
    }

    @Test
    void shouldThrowExceptionWhenTechnologiesAreLessThanMinimum() {
        // Given
        List<Technology> technologies = List.of(
                new Technology(1L, "Java"),
                new Technology(2L, "Spring")
        );
        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", technologies);

        // When / Then
        assertThrows(ValueNotValidException.class, () -> Validator.validate(capability), "Minimum amount of technologies not met");
    }

    @Test
    void shouldThrowExceptionWhenTechnologiesExceedMaximum() {
        // Given
        List<Technology> technologies = new ArrayList<>();
        for (int i = 1; i <= 21; i++) {
            technologies.add(new Technology((long) i, "Tech" + i));
        }
        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", technologies);

        // When / Then
        assertThrows(ValueNotValidException.class, () -> Validator.validate(capability), "Maximum amount of technologies exceeded");
    }

    @Test
    void shouldRemoveDuplicatedTechnologiesByName() {
        // Given
        List<Technology> technologies = List.of(
                new Technology(1L, "Java"),
                new Technology(2L, "Java"),  // Duplicate
                new Technology(2L, "Python"),  // Duplicate
                new Technology(3L, "Spring Boot")
        );
        CapabilityPlusTechnologiesModel capability = new CapabilityPlusTechnologiesModel(1L, "Backend Development", "Backend Capability", technologies);

        // When
        CapabilityPlusTechnologiesModel result = Validator.validate(capability);

        // Then
        assertThat(result.getTechnologies()).hasSize(3);
        assertThat(result.getTechnologiesNames()).containsExactlyInAnyOrder("Java","Python", "Spring Boot");
    }
}