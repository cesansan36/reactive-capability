package com.rutaaprendizajewebflux.capability.infrastructure.secondary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamp_capability")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BootcampCapabilityEntity {

    @Id
    private Long id;

    @Column("bootcamp_id")
    private Long bootcampId;

    @Column("capability_id")
    private Long capabilityId;
}
