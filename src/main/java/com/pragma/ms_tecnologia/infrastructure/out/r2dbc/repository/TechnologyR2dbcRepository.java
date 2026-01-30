package com.pragma.ms_tecnologia.infrastructure.out.r2dbc.repository;

import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface TechnologyR2dbcRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {

    Mono<Boolean> existsByName(String name);

    @Query("SELECT COUNT(*) FROM technologies WHERE id IN (:ids)")
    Mono<Long> countByIds(@Param("ids") List<Long> ids);

    Flux<TechnologyEntity> findByIdIn(List<Long> ids);
}
