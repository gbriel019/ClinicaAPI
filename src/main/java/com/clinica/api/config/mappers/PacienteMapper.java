package com.clinica.api.config.mappers;

import com.clinica.api.dto.request.PacienteRequest;
import com.clinica.api.dto.response.PacienteResponse;
import com.clinica.api.entities.Paciente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "logradouro", ignore = true)
    @Mapping(target = "bairro", ignore = true)
    @Mapping(target = "cidade", ignore = true)
    @Mapping(target = "uf", ignore = true)
    Paciente toEntity(PacienteRequest request);

    PacienteResponse toResponse(Paciente paciente);
}