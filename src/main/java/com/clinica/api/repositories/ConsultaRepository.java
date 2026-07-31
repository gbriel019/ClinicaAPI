package com.clinica.api.repositories;

import com.clinica.api.entities.Consulta;
import com.clinica.api.entities.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);
}
