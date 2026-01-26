package com.pragma.ms_tecnologia.domain.usecase;

import com.pragma.ms_tecnologia.domain.api.ITechnologyServicePort;
import com.pragma.ms_tecnologia.domain.exceptions.TechnologyAlreadyExistsException;
import com.pragma.ms_tecnologia.domain.model.Technology;
import com.pragma.ms_tecnologia.domain.spi.ITechnologyPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
}
