package com.clinica.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicoRequest {

    @NotBlank(message = "O nome do médico é obrigatório")
    private String nome;

    @NotBlank(message = "O CRM do médico é obrigatório")
    private String crm;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail é inválido")
    private String email;

    private String telefone;

    @NotNull(message = "A especialidade é obrigatória")
    private Long especialidadeId;
}