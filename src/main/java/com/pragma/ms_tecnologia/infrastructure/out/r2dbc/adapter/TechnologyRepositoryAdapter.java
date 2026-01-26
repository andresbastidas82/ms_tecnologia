package com.pragma.ms_tecnologia.infrastructure.out.r2dbc.adapter;

import com.pragma.ms_tecnologia.domain.model.Technology;
import com.pragma.ms_tecnologia.domain.spi.ITechnologyPersistencePort;
import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.entity.TechnologyEntity;
import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.mapper.ITechnologyMapper;
import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.repository.TechnologyR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
        TechnologyEntity technologyEntity = technologyMapper.toTechnologyEntity(technology);
        return technologyR2dbcRepository.save(technologyEntity).map(technologyMapper::toTechnology);
    }
}
