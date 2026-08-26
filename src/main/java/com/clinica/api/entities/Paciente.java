package com.clinica.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "paciente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do paciente é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O campo CPF é obrigatório")
    @Column(nullable = false, unique = true)
    private String cpf;

    @NotBlank(message = "O campo E-mail é obrigatório")
    @Email(message = "O e-mail é inválido")
    @Column(nullable = false, unique = true)
    private String email;

    private String telefone;

    private LocalDate dataNascimento;

    @Column(nullable = false)
    private Boolean ativo = true;

    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;

}
