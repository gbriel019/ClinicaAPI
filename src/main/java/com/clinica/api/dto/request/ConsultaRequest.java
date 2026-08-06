package com.clinica.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ConsultaRequest {

    @NotNull
    private Long medicoId;

    @NotNull
    private Long pacienteId;

    @NotNull
    private LocalDateTime dataHora;

}