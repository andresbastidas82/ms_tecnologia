package com.pragma.ms_tecnologia.infrastructure.input.rest.handler;

import com.pragma.ms_tecnologia.application.dto.TechnologyRequest;
import com.pragma.ms_tecnologia.application.dto.TechnologyResponse;
import com.pragma.ms_tecnologia.application.helper.ITechnologyHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TechnologyHandler {

    private final ITechnologyHelper technologyHelper;

    public Mono<ServerResponse> createTechnology(ServerRequest request) {
        return request.bodyToMono(TechnologyRequest.class)
                .flatMap(technologyHelper::save)
                .flatMap(response ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response)
                );
    }

    public Mono<ServerResponse> validateTechnologies(ServerRequest request) {
        return request.bodyToMono(List.class)
                .flatMap(technologyHelper::existAllByIds)
                .flatMap(response ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response)
                );
    }

    public Mono<ServerResponse> getTechnologySummaries(ServerRequest request) {
        List<Long> ids = request.queryParam("ids")
                .map(value ->
                        Arrays.stream(value.split(","))
                                .map(String::trim)
                                .map(Long::valueOf)
                                .toList()
                )
                .orElseThrow(() -> new IllegalArgumentException("ids es requerido"));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(technologyHelper.getTechnologiesByIds(ids), TechnologyResponse.class);
    }

    public Mono<ServerResponse> deleteTechnologies(ServerRequest request) {
        List<Long> ids = request.queryParam("ids")
                .map(value ->
                        Arrays.stream(value.split(","))
                                .map(String::trim)
                                .map(Long::valueOf)
                                .toList()
                )
                .orElseThrow(() -> new IllegalArgumentException("ids es requerido"));

        return technologyHelper.deleteTechnologies(ids)
                .onErrorReturn(false)
                .flatMap(response ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response)
                        );

    }


}
