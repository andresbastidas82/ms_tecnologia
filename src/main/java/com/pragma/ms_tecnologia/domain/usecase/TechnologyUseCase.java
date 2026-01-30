package com.pragma.ms_tecnologia.domain.usecase;

import com.pragma.ms_tecnologia.domain.api.ITechnologyServicePort;
import com.pragma.ms_tecnologia.domain.exceptions.BadRequestException;
import com.pragma.ms_tecnologia.domain.exceptions.TechnologyAlreadyExistsException;
import com.pragma.ms_tecnologia.domain.model.Technology;
import com.pragma.ms_tecnologia.domain.spi.ITechnologyPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.pragma.ms_tecnologia.domain.utils.Constants.DESCRIPTION_IS_REQUIRED;
import static com.pragma.ms_tecnologia.domain.utils.Constants.INVALID_DESCRIPTION;
import static com.pragma.ms_tecnologia.domain.utils.Constants.INVALID_NAME;
import static com.pragma.ms_tecnologia.domain.utils.Constants.NAME_IS_REQUIRED;

@Service
@RequiredArgsConstructor
public class TechnologyUseCase implements ITechnologyServicePort {

    private final ITechnologyPersistencePort technologyPersistencePort;

    public Mono<Technology> save(Technology technology) {
        return validateBusinessRules(technology)
                .then(technologyPersistencePort.save(technology));
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

    @Override
    public Mono<Boolean> deleteTechnologies(List<Long> ids) {
        return technologyPersistencePort.deleteTechnologies(ids);
    }

    private Mono<Void> validateBusinessRules(Technology technology) {
        List<String> errors = getListErrors(technology);
        if (!errors.isEmpty()) {
            return Mono.error(new BadRequestException(String.join("|", errors)));
        }
        return technologyPersistencePort.existsByName(technology.getName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new TechnologyAlreadyExistsException());
                    }
                    return Mono.empty();
                });
    }

    private static List<String> getListErrors(Technology technology) {
        List<String> errors = new ArrayList<>();
        if(technology.getName() == null || technology.getName().isEmpty()) {
            errors.add(NAME_IS_REQUIRED);
        }
        if(technology.getDescription() == null || technology.getDescription().isEmpty()) {
            errors.add(DESCRIPTION_IS_REQUIRED);
        }
        if(technology.getName() != null && technology.getName().length() > 50) {
            errors.add(INVALID_NAME);
        }
        if(technology.getDescription() != null && technology.getDescription().length() > 90) {
            errors.add(INVALID_DESCRIPTION);
        }
        return errors;
    }

}
