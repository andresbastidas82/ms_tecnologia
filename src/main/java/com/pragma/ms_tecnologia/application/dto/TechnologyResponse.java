package com.pragma.ms_tecnologia.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyResponse {
    private Long id;
    private String name;
    private String description;
}
