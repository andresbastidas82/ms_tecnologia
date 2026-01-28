package com.pragma.ms_tecnologia.domain.usecase;

import com.pragma.ms_tecnologia.domain.api.ITechnologyServicePort;
import com.pragma.ms_tecnologia.domain.exceptions.TechnologyAlreadyExistsException;
import com.pragma.ms_tecnologia.domain.model.Technology;
import com.pragma.ms_tecnologia.domain.spi.ITechnologyPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyUseCase implements ITechnologyServicePort {

    private final ITechnologyPersistencePort technologyPersistencePort;

    public Mono<Technology> save(Technology technology) {
        return technologyPersistencePort.existsByName(technology.getName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new TechnologyAlreadyExistsException());
                    }
                    return technologyPersistencePort.save(technology);
                });
    }

    @Override
    public Mono<Boolean> existAllByIds(List<Long> technologyIds) {
        if (technologyIds == null || technologyIds.isEmpty()) {
            return Mono.just(false);
        }

        return technologyPersistencePort.countByIds(technologyIds)
                .map(count -> count == technologyIds.size());
    }

    @Override
    public Flux<Technology> getTechnologiesByIds(List<Long> ids) {
        return technologyPersistencePort.getTechnologiesByIds(ids);
    }
}
