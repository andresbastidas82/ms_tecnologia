package com.pragma.ms_tecnologia.infrastructure.out.r2dbc.repository;

import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface TechnologyR2dbcRepository extends ReactiveCrudRepository<TechnologyEntity, UUID> {

    Mono<Boolean> existsByName(String name);
}
