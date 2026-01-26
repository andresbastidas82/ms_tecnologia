package com.pragma.ms_tecnologia.domain.spi;

import com.pragma.ms_tecnologia.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyPersistencePort {

    Mono<Boolean> existsByName(String name);

    Mono<Technology> save(Technology technology);
}
