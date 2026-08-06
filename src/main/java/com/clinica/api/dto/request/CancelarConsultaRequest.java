package com.clinica.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelarConsultaRequest {

    @NotNull
    private String motivo;

}