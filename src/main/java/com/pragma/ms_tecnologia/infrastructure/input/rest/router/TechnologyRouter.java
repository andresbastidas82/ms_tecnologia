package com.pragma.ms_tecnologia.infrastructure.input.rest.router;

import com.pragma.ms_tecnologia.infrastructure.input.rest.handler.TechnologyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TechnologyRouter {

    @Bean
    public RouterFunction<ServerResponse> technologyRoutes(TechnologyHandler handler) {
        return route(POST("/api/v1/technology"), handler::createTechnology)
                .andRoute(POST("/api/v1/technology/validate"), handler::validateTechnologies)
                .andRoute(GET("/api/v1/technology/byIds"), handler::getTechnologySummaries)
                .andRoute(DELETE("/api/v1/technology"), handler::deleteTechnologies);
    }
}
