package com.pragma.ms_tecnologia.infrastructure.out.r2dbc.adapter;

import com.pragma.ms_tecnologia.domain.model.Technology;
import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.entity.TechnologyEntity; // Asegúrate de importar tu entidad correcta
import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.mapper.ITechnologyMapper;
import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.repository.TechnologyR2dbcRepository;
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
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyRepositoryAdapterTest {

    @Mock
    private TechnologyR2dbcRepository technologyR2dbcRepository;

    @Mock
    private ITechnologyMapper technologyMapper;

    @InjectMocks
    private TechnologyRepositoryAdapter technologyRepositoryAdapter;

    // --- TEST: existsByName ---

    @Test
    @DisplayName("ExistsByName: Should return true when repository returns true")
    void existsByName_WhenFound_ShouldReturnTrue() {
        // Arrange
        String name = "Java";
        when(technologyR2dbcRepository.existsByName(name)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = technologyRepositoryAdapter.existsByName(name);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(technologyR2dbcRepository).existsByName(name);
    }

    // --- TEST: save ---

    @Test
    @DisplayName("Save: Should map to entity, save, and map back to model")
    void save_ShouldPersistAndReturnModel() {
        // Arrange
        Technology domainModel = new Technology(null, "Java", "Desc");
        // Nota: Asumo que tienes una clase TechnologyEntity. Si usas Builder o setters, ajústalo.
        TechnologyEntity entity = new TechnologyEntity();
        entity.setName("Java");

        TechnologyEntity savedEntity = new TechnologyEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Java");

        Technology savedModel = new Technology(1L, "Java", "Desc");

        // 1. Mapeo de Dominio -> Entidad
        when(technologyMapper.toTechnologyEntity(domainModel)).thenReturn(entity);
        // 2. Guardado en Repositorio
        when(technologyR2dbcRepository.save(entity)).thenReturn(Mono.just(savedEntity));
        // 3. Mapeo de Entidad -> Dominio
        when(technologyMapper.toTechnology(savedEntity)).thenReturn(savedModel);

        // Act
        Mono<Technology> result = technologyRepositoryAdapter.save(domainModel);

        // Assert
        StepVerifier.create(result)
                .expectNext(savedModel)
                .verifyComplete();

        verify(technologyMapper).toTechnologyEntity(domainModel);
        verify(technologyR2dbcRepository).save(entity);
        verify(technologyMapper).toTechnology(savedEntity);
    }

    // --- TEST: countByIds ---

    @Test
    @DisplayName("CountByIds: Should return count from repository")
    void countByIds_ShouldReturnCount() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(technologyR2dbcRepository.countByIds(ids)).thenReturn(Mono.just(3L));

        // Act
        Mono<Long> result = technologyRepositoryAdapter.countByIds(ids);

        // Assert
        StepVerifier.create(result)
                .expectNext(3L)
                .verifyComplete();

        verify(technologyR2dbcRepository).countByIds(ids);
    }

    // --- TEST: getTechnologiesByIds ---

    @Test
    @DisplayName("GetTechnologiesByIds: Should return Flux of models")
    void getTechnologiesByIds_ShouldReturnFlux() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);

        TechnologyEntity entity1 = new TechnologyEntity();
        entity1.setId(1L);
        TechnologyEntity entity2 = new TechnologyEntity();
        entity2.setId(2L);

        Technology model1 = new Technology(1L, "A", "D");
        Technology model2 = new Technology(2L, "B", "D");

        when(technologyR2dbcRepository.findByIdIn(ids)).thenReturn(Flux.just(entity1, entity2));
        when(technologyMapper.toTechnology(entity1)).thenReturn(model1);
        when(technologyMapper.toTechnology(entity2)).thenReturn(model2);

        // Act
        Flux<Technology> result = technologyRepositoryAdapter.getTechnologiesByIds(ids);

        // Assert
        StepVerifier.create(result)
                .expectNext(model1)
                .expectNext(model2)
                .verifyComplete();

        verify(technologyR2dbcRepository).findByIdIn(ids);
    }

    // --- TEST: deleteTechnologies ---

    @Test
    @DisplayName("DeleteTechnologies: Should return true on success")
    void deleteTechnologies_ShouldReturnTrue() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);
        // El repositorio devuelve Mono<Void> al borrar
        when(technologyR2dbcRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        // Act
        Mono<Boolean> result = technologyRepositoryAdapter.deleteTechnologies(ids);

        // Assert
        StepVerifier.create(result)
                .expectNext(true) // El adaptador tiene .thenReturn(true)
                .verifyComplete();

        verify(technologyR2dbcRepository).deleteAllById(ids);
    }

    @Test
    @DisplayName("DeleteTechnologies: Should propagate error on failure")
    void deleteTechnologies_OnError_ShouldPropagateError() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);
        when(technologyR2dbcRepository.deleteAllById(ids))
                .thenReturn(Mono.error(new RuntimeException("DB Error")));

        // Act
        Mono<Boolean> result = technologyRepositoryAdapter.deleteTechnologies(ids);

        // Assert
        StepVerifier.create(result)
                .expectErrorMessage("DB Error")
                .verify();
    }
}