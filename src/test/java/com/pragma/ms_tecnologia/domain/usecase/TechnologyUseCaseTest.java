package com.pragma.ms_tecnologia.domain.usecase;

import com.pragma.ms_tecnologia.domain.exceptions.TechnologyAlreadyExistsException;
import com.pragma.ms_tecnologia.domain.model.Technology;
import com.pragma.ms_tecnologia.domain.spi.ITechnologyPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    @Mock
    private ITechnologyPersistencePort technologyPersistencePort;

    @InjectMocks
    private TechnologyUseCase technologyUseCase;

    @Test
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
    void save_WhenTechnologyAlreadyExists_ShouldReturnError() {
        // Arrange
        Technology technology = new Technology(null, "Python", "Lenguaje de script");

        when(technologyPersistencePort.existsByName(technology.getName()))
                .thenReturn(Mono.just(true));

        // Act
        Mono<Technology> result = technologyUseCase.save(technology);

        // Assert
        StepVerifier.create(result)
                .expectError(TechnologyAlreadyExistsException.class)
                .verify();

        verify(technologyPersistencePort).existsByName(technology.getName());
        // Verificamos que NUNCA se llame a guardar si ya existe
        verify(technologyPersistencePort, never()).save(any(Technology.class));
    }
}