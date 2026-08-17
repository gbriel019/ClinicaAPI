package com.clinica.api.entities;

import com.clinica.api.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O campo nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O campo E-mail é obrigatório")
    @Email(message = "O e-mail é invalido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "O campo Senha é obrigatório")
    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean ativo = true;

    private Integer tentativasFalhas = 0;

    private LocalDateTime bloqueadoAte;

}
