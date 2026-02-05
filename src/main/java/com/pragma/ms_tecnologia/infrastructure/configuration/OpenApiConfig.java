package com.pragma.ms_tecnologia.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Tecnologias API",
                version = "1.0",
                description = "API de gestión de tecnologias"
        )
)
public class OpenApiConfig {
}