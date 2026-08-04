package com.clinica.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicoResponse {

    private Long id;

    private String nome;

    private String crm;

    private String email;

    private String telefone;

    private EspecialidadeResponse especialidade;

    private Boolean ativo;
}