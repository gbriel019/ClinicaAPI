package com.clinica.api.config.mappers;

import com.clinica.api.dto.request.ConsultaRequest;
import com.clinica.api.dto.response.ConsultaResponse;
import com.clinica.api.entities.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medico", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "motivoCancelamento", ignore = true)
    Consulta toEntity(ConsultaRequest request);

    @Mapping(target = "medicoId", source = "medico.id")
    @Mapping(target = "nomeMedico", source = "medico.nome")
    @Mapping(target = "pacienteId", source = "paciente.id")
    @Mapping(target = "nomePaciente", source = "paciente.nome")
    ConsultaResponse toResponse(Consulta consulta);
}