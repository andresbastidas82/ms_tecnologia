package com.pragma.ms_tecnologia.application.helper;

import com.pragma.ms_tecnologia.application.dto.TechnologyRequest;
import com.pragma.ms_tecnologia.application.dto.TechnologyResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyHelper {

    Mono<TechnologyResponse> save(TechnologyRequest technologyRequest);
    Mono<Boolean> existAllByIds(List<Long> technologyIds);
    Flux<TechnologyResponse> getTechnologiesByIds(List<Long> ids);
}
