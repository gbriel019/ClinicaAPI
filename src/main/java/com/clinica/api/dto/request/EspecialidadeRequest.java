package com.clinica.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class EspecialidadeRequest {
    @NotBlank(message = "O nome da especialidade é obrigatório")
    private String nome;
}
