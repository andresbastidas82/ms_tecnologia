package com.pragma.ms_tecnologia.domain.spi;

import com.pragma.ms_tecnologia.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyPersistencePort {

    Mono<Boolean> existsByName(String name);

    Mono<Technology> save(Technology technology);

    Mono<Long> countByIds(List<Long> ids);

    Flux<Technology> getTechnologiesByIds(List<Long> ids);

    Mono<Boolean> deleteTechnologies(List<Long> ids);
}
