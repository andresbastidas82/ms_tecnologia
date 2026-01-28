package com.pragma.ms_tecnologia.infrastructure.input.rest;

import com.pragma.ms_tecnologia.application.dto.TechnologyRequest;
import com.pragma.ms_tecnologia.application.dto.TechnologyResponse;
import com.pragma.ms_tecnologia.application.helper.ITechnologyHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/technology")
public class TechnologyController {

    private final ITechnologyHelper technologyHelper;

    public TechnologyController(ITechnologyHelper technologyHelper) {
        this.technologyHelper = technologyHelper;
    }

    @PostMapping
    public Mono<ResponseEntity<TechnologyResponse>> createTechnology(@Valid @RequestBody TechnologyRequest technologyRequest) {
        return technologyHelper.save(technologyRequest).map(ResponseEntity::ok);
    }

    @PostMapping("/validate")
    public Mono<Boolean> validateTechnologies(@RequestBody List<Long> technologyIds) {
        return technologyHelper.existAllByIds(technologyIds);
    }

    @GetMapping("/byIds")
    public Flux<TechnologyResponse> getTechnologySummaries(@RequestParam List<Long> ids) {
        return technologyHelper.getTechnologiesByIds(ids);
    }
}
