package com.pragma.ms_tecnologia.infrastructure.out.r2dbc.repository;

import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;


public interface TechnologyR2dbcRepository extends ReactiveCrudRepository<TechnologyEntity, UUID> {

    Mono<Boolean> existsByName(String name);

    @Query("SELECT COUNT(*) FROM technologies WHERE id IN (:ids)")
    Mono<Long> countByIds(@Param("ids") List<Long> ids);
}
