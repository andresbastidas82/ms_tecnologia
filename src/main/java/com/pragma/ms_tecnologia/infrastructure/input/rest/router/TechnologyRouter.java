package com.pragma.ms_tecnologia.infrastructure.input.rest.router;

import com.pragma.ms_tecnologia.application.dto.TechnologyRequest;
import com.pragma.ms_tecnologia.infrastructure.input.rest.handler.TechnologyHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TechnologyRouter {

    @Bean
    @RouterOperations({

            @RouterOperation(
                    path = "/api/v1/technology",
                    method = RequestMethod.POST,
                    beanClass = TechnologyHandler.class,
                    beanMethod = "createTechnology",
                    operation = @Operation(
                            operationId = "createTechnology",
                            summary = "Registrar una tecnologia",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = TechnologyRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Tecnologia creada")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/technology/validate",
                    method = RequestMethod.POST,
                    beanClass = TechnologyHandler.class,
                    beanMethod = "validateTechnologies",
                    operation = @Operation(
                            operationId = "validateTechnologies",
                            summary = "Validar tecnologias si existen",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = List.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Tecnologias validadas")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/technology/byIds",
                    method = RequestMethod.GET,
                    beanClass = TechnologyHandler.class,
                    beanMethod = "getTechnologySummaries",
                    operation = @Operation(
                            operationId = "getTechnologySummaries",
                            summary = "Obtener tecnologias por ids",
                            parameters = {
                                    @Parameter(name = "ids", description = "Lista de ids de tecnologias", required = true)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de tecnologias")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/technology",
                    method = RequestMethod.DELETE,
                    beanClass = TechnologyHandler.class,
                    beanMethod = "deleteTechnologies",
                    operation = @Operation(
                            operationId = "deleteTechnologies",
                            summary = "Eliminar tecnologias por ids",
                            parameters = {
                                    @Parameter(name = "ids", description = "Lista de ids de tecnologias", required = true)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Tecnologias eliminadas")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> technologyRoutes(TechnologyHandler handler) {
        return route(POST("/api/v1/technology"), handler::createTechnology)
                .andRoute(POST("/api/v1/technology/validate"), handler::validateTechnologies)
                .andRoute(GET("/api/v1/technology/byIds"), handler::getTechnologySummaries)
                .andRoute(DELETE("/api/v1/technology"), handler::deleteTechnologies);
    }
}
