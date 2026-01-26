package com.pragma.ms_tecnologia.application.handler;

import com.pragma.ms_tecnologia.application.dto.TechnologyRequest;
import com.pragma.ms_tecnologia.application.dto.TechnologyResponse;
import reactor.core.publisher.Mono;

public interface ITechnologyHelper {

    Mono<TechnologyResponse> save(TechnologyRequest technologyRequest);
}
