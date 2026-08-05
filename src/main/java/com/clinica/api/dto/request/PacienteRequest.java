package com.clinica.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PacienteRequest {

    @NotBlank(message = "O nome do paciente é obrigatório")
    private String nome;

    @NotBlank(message = "O campo CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "O campo E-mail é obrigatório")
    @Email(message = "O e-mail é inválido")
    private String email;

    private String telefone;

    private LocalDate dataNascimento;
}
