package com.pragma.ms_tecnologia.infrastructure.input.rest;

import com.pragma.ms_tecnologia.application.dto.TechnologyRequest;
import com.pragma.ms_tecnologia.application.dto.TechnologyResponse;
import com.pragma.ms_tecnologia.application.handler.ITechnologyHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
}
