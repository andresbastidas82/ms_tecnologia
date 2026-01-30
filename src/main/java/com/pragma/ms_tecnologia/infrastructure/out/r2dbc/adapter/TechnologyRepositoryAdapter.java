package com.pragma.ms_tecnologia.infrastructure.out.r2dbc.adapter;

import com.pragma.ms_tecnologia.domain.model.Technology;
import com.pragma.ms_tecnologia.domain.spi.ITechnologyPersistencePort;
import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.mapper.ITechnologyMapper;
import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.repository.TechnologyR2dbcRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TechnologyRepositoryAdapter implements ITechnologyPersistencePort {

    private final TechnologyR2dbcRepository technologyR2dbcRepository;
    private final ITechnologyMapper technologyMapper;

    public TechnologyRepositoryAdapter(TechnologyR2dbcRepository technologyR2dbcRepository, ITechnologyMapper technologyMapper) {
        this.technologyR2dbcRepository = technologyR2dbcRepository;
        this.technologyMapper = technologyMapper;
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return technologyR2dbcRepository.existsByName(name);
    }

    @Override
    public Mono<Technology> save(Technology technology) {
        return Mono.just(technology)
                .map(technologyMapper::toTechnologyEntity)
                .flatMap(technologyR2dbcRepository::save)
                .map(technologyMapper::toTechnology);
    }

    @Override
    public Mono<Long> countByIds(List<Long> ids) {
        return technologyR2dbcRepository.countByIds(ids);
    }

    @Override
    public Flux<Technology> getTechnologiesByIds(List<Long> ids) {
        return technologyR2dbcRepository.findByIdIn(ids)
                .map(technologyMapper::toTechnology);
    }

    @Transactional
    @Override
    public Mono<Boolean> deleteTechnologies(List<Long> ids) {
        return technologyR2dbcRepository.deleteAllById(ids)
                .thenReturn(true)
                .onErrorResume(Mono::error);
    }

}
