package com.pragma.ms_tecnologia.infrastructure.out.r2dbc.mapper;

import com.pragma.ms_tecnologia.domain.model.Technology;
import com.pragma.ms_tecnologia.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITechnologyMapper {

    Technology toTechnology(TechnologyEntity technologyEntity);

    TechnologyEntity toTechnologyEntity(Technology technology);
}
