package com.pragma.ms_tecnologia.domain.api;

import com.pragma.ms_tecnologia.domain.model.Technology;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyServicePort {

    Mono<Technology> save(Technology technology);

    Mono<Boolean> existAllByIds(List<Long> technologyIds);
}
