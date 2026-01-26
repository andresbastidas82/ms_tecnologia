package com.pragma.ms_tecnologia.domain.api;

import com.pragma.ms_tecnologia.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyServicePort {

    Mono<Technology> save(Technology technology);
}
