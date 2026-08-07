package com.clinica.api.dto.request;

import com.clinica.api.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequest {

    @NotBlank(message = "O campo nome é obrigatório")
    private String nome;

    @NotBlank(message = "O campo E-mail é obrigatório")
    @Email(message = "O e-mail é inválido")
    private String email;

    @NotBlank(message = "O campo Senha é obrigatório")
    private String senha;

    private Role role;

    private Boolean ativo = true;

}