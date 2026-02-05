package com.pragma.ms_tecnologia.infrastructure.input.rest.handler;

import com.pragma.ms_tecnologia.application.dto.TechnologyRequest;
import com.pragma.ms_tecnologia.application.dto.TechnologyResponse;
import com.pragma.ms_tecnologia.application.helper.ITechnologyHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyHandlerTest {

    @Mock
    private ITechnologyHelper technologyHelper;

    @InjectMocks
    private TechnologyHandler technologyHandler;

    // --- TEST: createTechnology ---

    @Test
    @DisplayName("Create Technology: Should return 200 OK when successful")
    void createTechnology_ShouldReturnOk() {
        // Arrange
        TechnologyRequest requestDto = new TechnologyRequest();
        requestDto.setName("Java");

        // Asumimos que el helper retorna un TechnologyResponse o similar
        TechnologyResponse responseDto = new TechnologyResponse();

        MockServerRequest request = MockServerRequest.builder()
                .body(Mono.just(requestDto));

        when(technologyHelper.save(any(TechnologyRequest.class)))
                .thenReturn(Mono.just(responseDto));

        // Act
        Mono<ServerResponse> result = technologyHandler.createTechnology(request);

        // Assert
        StepVerifier.create(result)
                .assertNext(response -> assertEquals(HttpStatus.OK, response.statusCode()))
                .verifyComplete();

        verify(technologyHelper).save(any(TechnologyRequest.class));
    }

    // --- TEST: validateTechnologies ---

    @Test
    @DisplayName("Validate Technologies: Should return 200 OK with boolean result")
    void validateTechnologies_ShouldReturnOk() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);

        MockServerRequest request = MockServerRequest.builder()
                .body(Mono.just(ids));

        when(technologyHelper.existAllByIds(anyList()))
                .thenReturn(Mono.just(true));

        // Act
        Mono<ServerResponse> result = technologyHandler.validateTechnologies(request);

        // Assert
        StepVerifier.create(result)
                .assertNext(response -> assertEquals(HttpStatus.OK, response.statusCode()))
                .verifyComplete();

        verify(technologyHelper).existAllByIds(anyList());
    }

    // --- TESTS: getTechnologySummaries ---

    @Test
    @DisplayName("Get Summaries: Should return 200 OK when ids param is present")
    void getTechnologySummaries_ShouldReturnOk() {
        // Arrange
        String idsParam = "1, 2, 3";
        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);
        TechnologyResponse responseDto = new TechnologyResponse();

        MockServerRequest request = MockServerRequest.builder()
                .queryParam("ids", idsParam)
                .build();

        when(technologyHelper.getTechnologiesByIds(expectedIds))
                .thenReturn(Flux.just(responseDto));

        // Act
        Mono<ServerResponse> result = technologyHandler.getTechnologySummaries(request);

        // Assert
        StepVerifier.create(result)
                .assertNext(response -> assertEquals(HttpStatus.OK, response.statusCode()))
                .verifyComplete();

        verify(technologyHelper).getTechnologiesByIds(expectedIds);
    }

    @Test
    @DisplayName("Get Summaries: Should throw IllegalArgumentException when ids param is missing")
    void getTechnologySummaries_WhenIdsMissing_ShouldThrowException() {
        // Arrange
        MockServerRequest request = MockServerRequest.builder()
                // Sin query param "ids"
                .build();

        // Act & Assert
        // Como usas .orElseThrow(), la excepción salta antes de crear el Mono
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            technologyHandler.getTechnologySummaries(request);
        });

        assertEquals("ids es requerido", exception.getMessage());
    }

    // --- TESTS: deleteTechnologies ---

    @Test
    @DisplayName("Delete Technologies: Should return 200 OK with true when successful")
    void deleteTechnologies_ShouldReturnOk() {
        // Arrange
        String idsParam = "1, 2";
        List<Long> expectedIds = Arrays.asList(1L, 2L);

        MockServerRequest request = MockServerRequest.builder()
                .queryParam("ids", idsParam)
                .build();

        when(technologyHelper.deleteTechnologies(expectedIds))
                .thenReturn(Mono.just(true));

        // Act
        Mono<ServerResponse> result = technologyHandler.deleteTechnologies(request);

        // Assert
        StepVerifier.create(result)
                .assertNext(response -> assertEquals(HttpStatus.OK, response.statusCode()))
                .verifyComplete();

        verify(technologyHelper).deleteTechnologies(expectedIds);
    }

    @Test
    @DisplayName("Delete Technologies: Should return 200 OK with false when helper fails")
    void deleteTechnologies_OnError_ShouldReturnFalse() {
        // Arrange
        String idsParam = "1";
        List<Long> expectedIds = Arrays.asList(1L);

        MockServerRequest request = MockServerRequest.builder()
                .queryParam("ids", idsParam)
                .build();

        // Simulamos error en el helper
        when(technologyHelper.deleteTechnologies(expectedIds))
                .thenReturn(Mono.error(new RuntimeException("Error interno")));

        // Act
        Mono<ServerResponse> result = technologyHandler.deleteTechnologies(request);

        // Assert
        // Gracias a .onErrorReturn(false), esperamos un 200 OK (con body false)
        StepVerifier.create(result)
                .assertNext(response -> assertEquals(HttpStatus.OK, response.statusCode()))
                .verifyComplete();

        verify(technologyHelper).deleteTechnologies(expectedIds);
    }

    @Test
    @DisplayName("Delete Technologies: Should throw IllegalArgumentException when ids param is missing")
    void deleteTechnologies_WhenIdsMissing_ShouldThrowException() {
        // Arrange
        MockServerRequest request = MockServerRequest.builder()
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            technologyHandler.deleteTechnologies(request);
        });

        assertEquals("ids es requerido", exception.getMessage());
    }
}