package com.pragma.ms_tecnologia.application.handler.impl;

import com.pragma.ms_tecnologia.application.dto.TechnologyRequest;
import com.pragma.ms_tecnologia.application.dto.TechnologyResponse;
import com.pragma.ms_tecnologia.application.handler.ITechnologyHelper;
import com.pragma.ms_tecnologia.application.mapper.ITechnologyRequestMapper;
import com.pragma.ms_tecnologia.domain.api.ITechnologyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TechnologyHelperImpl implements ITechnologyHelper {

    private final ITechnologyServicePort technologyServicePort;
    private final ITechnologyRequestMapper technologyRequestMapper;

    @Override
    public Mono<TechnologyResponse> save(TechnologyRequest technologyRequest) {
        return Mono.just(technologyRequest)
                .map(technologyRequestMapper::toTechnology)
                .flatMap(technologyServicePort::save)
                .map(technologyRequestMapper::toTechnologyResponse);
    }
}
