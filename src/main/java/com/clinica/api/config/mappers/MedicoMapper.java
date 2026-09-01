package com.clinica.api.config.mappers;

import com.clinica.api.dto.request.MedicoRequest;
import com.clinica.api.dto.response.MedicoResponse;
import com.clinica.api.entities.Medico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = EspecialidadeMapper.class)
public interface MedicoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "especialidade", ignore = true)
    Medico toEntity(MedicoRequest request);

    MedicoResponse toResponse(Medico medico);
}