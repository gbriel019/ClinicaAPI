package com.clinica.api.dto.response;

import com.clinica.api.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponse {

    private Long id;

    private String nome;

    private String email;


    private Role role;

    private Boolean ativo = true;

}