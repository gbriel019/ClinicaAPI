
package com.clinica.api.config.mappers;

import com.clinica.api.dto.request.EspecialidadeRequest;
import com.clinica.api.dto.response.EspecialidadeResponse;
import com.clinica.api.entities.Especialidade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EspecialidadeMapper {

    @Mapping(target = "id", ignore = true)
    Especialidade toEntity(EspecialidadeRequest request);

    EspecialidadeResponse toResponse(Especialidade especialidade);
}