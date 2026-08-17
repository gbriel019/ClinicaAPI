package com.clinica.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ConsultaRequest {

    @NotNull
    private Long medicoId;

    @NotNull
    private Long pacienteId;

    @NotNull
    private LocalDateTime dataHora;

}