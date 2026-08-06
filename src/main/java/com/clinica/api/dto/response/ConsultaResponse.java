package com.clinica.api.dto.response;

import com.clinica.api.enums.StatusConsulta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ConsultaResponse {

    private Long id;

    private Long medicoId;

    private String nomeMedico;

    private Long pacienteId;

    private String nomePaciente;

    private LocalDateTime dataHora;

    private StatusConsulta status;

    private String motivoCancelamento;

}