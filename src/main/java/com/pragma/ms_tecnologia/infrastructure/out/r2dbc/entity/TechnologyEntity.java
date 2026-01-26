package com.pragma.ms_tecnologia.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("technologies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnologyEntity {

    @Id
    private Long id;
    private String name;
    private String description;
}