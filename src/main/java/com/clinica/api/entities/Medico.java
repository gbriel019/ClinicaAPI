package com.clinica.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do medico é obrigatório.")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O crm do médico é obrigatório")
    @Column(nullable = false ,unique = true)
    private String crm;

    @NotBlank(message = "O campo E-mail é obrigatório")
    @Email(message = "O e-mail é invalido")
    @Column(nullable = false, unique = true)
    private String email;

    private String telefone;

    @NotNull(message = "A especialidade é obrigatoria")
    @ManyToOne
    @JoinColumn(name = "especialidade_id", nullable = false)
    private Especialidade especialidade;

    @Column(nullable = false)
    private Boolean ativo = true;
}
