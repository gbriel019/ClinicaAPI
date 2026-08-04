package com.clinica.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EspecialidadeRequest {
    @NotBlank(message = "O nome da especialidade é obrigatório")
    private String nome;
}
