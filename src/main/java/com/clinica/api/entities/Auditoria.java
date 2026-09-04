package com.clinica.api.entities;


import com.clinica.api.enums.AcaoAuditoria;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;

    @Enumerated(EnumType.STRING)
    private AcaoAuditoria acao;

    private String entidade;

    private Long entidadeId;

    private LocalDateTime dataHora;
}
