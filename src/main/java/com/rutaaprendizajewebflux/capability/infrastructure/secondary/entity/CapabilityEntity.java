package com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("capability")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CapabilityEntity {
    @Id
    private Long id;
    private String name;
    private String description;

    @Override
    public String toString() {
        return "CapabilityEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
