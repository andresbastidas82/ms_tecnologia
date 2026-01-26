package com.pragma.ms_tecnologia.application.mapper;


import com.pragma.ms_tecnologia.application.dto.TechnologyRequest;
import com.pragma.ms_tecnologia.application.dto.TechnologyResponse;
import com.pragma.ms_tecnologia.domain.model.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITechnologyRequestMapper {

    Technology toTechnology(TechnologyRequest technologyRequest);

    TechnologyResponse toTechnologyResponse(Technology technology);
}
