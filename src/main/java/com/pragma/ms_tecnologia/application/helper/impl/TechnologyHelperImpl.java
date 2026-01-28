package com.pragma.ms_tecnologia.application.helper.impl;

import com.pragma.ms_tecnologia.application.dto.TechnologyRequest;
import com.pragma.ms_tecnologia.application.dto.TechnologyResponse;
import com.pragma.ms_tecnologia.application.helper.ITechnologyHelper;
import com.pragma.ms_tecnologia.application.mapper.ITechnologyRequestMapper;
import com.pragma.ms_tecnologia.domain.api.ITechnologyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Mono<Boolean> existAllByIds(List<Long> technologyIds) {
        return technologyServicePort.existAllByIds(technologyIds);
    }

    @Override
    public Flux<TechnologyResponse> getTechnologiesByIds(List<Long> ids) {
        return technologyServicePort.getTechnologiesByIds(ids)
                .map(technologyRequestMapper::toTechnologyResponse);
    }
}
