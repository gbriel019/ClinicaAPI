package com.clinica.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelarConsultaRequest {

    @NotNull
    private String motivo;

}