package com.pragma.ms_tecnologia.domain.usecase;

import com.pragma.ms_tecnologia.domain.exception.BadRequestException;
import com.pragma.ms_tecnologia.domain.exception.TechnologyAlreadyExistsException;
import com.pragma.ms_tecnologia.domain.model.Technology;
import com.pragma.ms_tecnologia.domain.spi.ITechnologyPersistencePort;
import com.pragma.ms_tecnologia.domain.utils.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    @Mock
    private ITechnologyPersistencePort technologyPersistencePort;

    @InjectMocks
    private TechnologyUseCase technologyUseCase;

    // --- TESTS PARA SAVE (Happy Path & Excepciones de Negocio) ---

    @Test
    @DisplayName("Save: Should save technology when valid")
    void save_WhenTechnologyDoesNotExist_ShouldSaveAndReturnTechnology() {
        // Arrange
        Technology technology = new Technology(1L, "Java", "Lenguaje de programación");

        when(technologyPersistencePort.existsByName(technology.getName()))
                .thenReturn(Mono.just(false));
        when(technologyPersistencePort.save(technology))
                .thenReturn(Mono.just(technology));

        // Act
        Mono<Technology> result = technologyUseCase.save(technology);

        // Assert
        StepVerifier.create(result)
                .expectNext(technology)
                .verifyComplete();

        verify(technologyPersistencePort).existsByName(technology.getName());
        verify(technologyPersistencePort).save(technology);
    }

    @Test
    @DisplayName("Save: Should throw TechnologyAlreadyExistsException when name exists")
    void save_WhenTechnologyAlreadyExists_ShouldReturnError() {
        // Arrange
        Technology technology = new Technology(null, "Python", "Lenguaje de script");

        when(technologyPersistencePort.existsByName(technology.getName()))
                .thenReturn(Mono.just(true));

        // Simulamos save para evitar NPE por evaluación eager del .then()
        when(technologyPersistencePort.save(any())).thenReturn(Mono.empty());

        // Act
        Mono<Technology> result = technologyUseCase.save(technology);

        // Assert
        StepVerifier.create(result)
                .expectError(TechnologyAlreadyExistsException.class)
                .verify();
    }

    // --- TESTS DE VALIDACIONES (BadRequestException) ---

    @Test
    @DisplayName("Save: Should throw BadRequestException when Name is null/empty")
    void save_WhenNameInvalid_ShouldThrowBadRequest() {
        // Arrange
        // 1. Instanciamos el objeto con datos inválidos (nombre vacío)
        Technology technology = new Technology(null, "", "Description");

        // 2. Simulamos el comportamiento del puerto.
        // Es necesario porque .then() evalúa su argumento inmediatamente.
        // Usamos any(Technology.class) para ser más específicos que any().
        when(technologyPersistencePort.save(any(Technology.class)))
                .thenReturn(Mono.empty());
        // Mono.empty() es válido aquí porque el flujo se cortará antes de usar este valor.

        // Act
        Mono<Technology> result = technologyUseCase.save(technology);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BadRequestException
                        // IMPORTANTE: Verifica que este texto sea idéntico al valor de tu constante NAME_IS_REQUIRED
                        && throwable.getMessage().equals(Constants.NAME_IS_REQUIRED))
                .verify();

        // Nota: No uses verifyNoInteractions(technologyPersistencePort) aquí,
        // porque el metodo save() SÍ se llama (para construir el Mono), aunque no se persista nada.
    }

    @Test
    @DisplayName("Save: Should throw BadRequestException when Description is too long")
    void save_WhenDescriptionTooLong_ShouldThrowBadRequest() {
        // Arrange
        String longDescription = "a".repeat(91); // 91 caracteres
        Technology technology = new Technology(null, "Java", longDescription);

        // Simulamos save para evitar NPE
        when(technologyPersistencePort.save(any())).thenReturn(Mono.empty());

        // Act
        Mono<Technology> result = technologyUseCase.save(technology);

        // Assert
        StepVerifier.create(result)
                .expectError(BadRequestException.class)
                .verify();
    }

    // --- TESTS PARA existAllByIds ---

    @Test
    @DisplayName("ExistAllByIds: Should return false when list is null or empty")
    void existAllByIds_WhenListEmpty_ShouldReturnFalse() {
        // Act & Assert
        StepVerifier.create(technologyUseCase.existAllByIds(null))
                .expectNext(false)
                .verifyComplete();

        StepVerifier.create(technologyUseCase.existAllByIds(Collections.emptyList()))
                .expectNext(false)
                .verifyComplete();

        verifyNoInteractions(technologyPersistencePort);
    }

    @Test
    @DisplayName("ExistAllByIds: Should return true when count matches list size")
    void existAllByIds_WhenAllExist_ShouldReturnTrue() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);
        when(technologyPersistencePort.countByIds(ids)).thenReturn(Mono.just(2L)); // Encontró 2

        // Act
        Mono<Boolean> result = technologyUseCase.existAllByIds(ids);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("ExistAllByIds: Should return false when count does not match list size")
    void existAllByIds_WhenSomeMissing_ShouldReturnFalse() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(technologyPersistencePort.countByIds(ids)).thenReturn(Mono.just(2L)); // Solo encontró 2 de 3

        // Act
        Mono<Boolean> result = technologyUseCase.existAllByIds(ids);

        // Assert
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    // --- TESTS PARA getTechnologiesByIds ---

    @Test
    @DisplayName("GetTechnologiesByIds: Should return Flux of technologies")
    void getTechnologiesByIds_ShouldReturnFlux() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);
        Technology t1 = new Technology(1L, "A", "D");
        Technology t2 = new Technology(2L, "B", "D");

        when(technologyPersistencePort.getTechnologiesByIds(ids)).thenReturn(Flux.just(t1, t2));

        // Act
        Flux<Technology> result = technologyUseCase.getTechnologiesByIds(ids);

        // Assert
        StepVerifier.create(result)
                .expectNext(t1)
                .expectNext(t2)
                .verifyComplete();

        verify(technologyPersistencePort).getTechnologiesByIds(ids);
    }

    // --- TESTS PARA deleteTechnologies ---

    @Test
    @DisplayName("DeleteTechnologies: Should delegate to persistence")
    void deleteTechnologies_ShouldReturnMonoBoolean() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);
        when(technologyPersistencePort.deleteTechnologies(ids)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = technologyUseCase.deleteTechnologies(ids);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(technologyPersistencePort).deleteTechnologies(ids);
    }
}