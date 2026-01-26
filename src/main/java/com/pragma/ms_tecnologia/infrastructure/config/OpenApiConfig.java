package com.pragma.ms_tecnologia.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI technologyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Technology Service API")
                        .description("Microservicio para la gestión de tecnologías del bootcamp")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Bootcamp Backend Team")
                                .email("backend@bootcamp.com")
                        )
                );
    }
}
