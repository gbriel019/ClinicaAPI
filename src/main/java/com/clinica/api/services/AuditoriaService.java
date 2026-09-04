package com.clinica.api.services;

import com.clinica.api.entities.Auditoria;
import com.clinica.api.enums.AcaoAuditoria;
import com.clinica.api.repositories.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public void registrar(
            AcaoAuditoria acao,
            String entidade,
            Long entidadeId
    ) {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String usuario = authentication.getName();

        Auditoria auditoria = Auditoria.builder()
                .usuario(usuario)
                .acao(acao)
                .entidade(entidade)
                .entidadeId(entidadeId)
                .dataHora(LocalDateTime.now())
                .build();

        auditoriaRepository.save(auditoria);

    }

}
