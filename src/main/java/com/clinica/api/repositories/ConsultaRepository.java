package com.clinica.api.repositories;

import com.clinica.api.entities.Consulta;
import com.clinica.api.entities.Medico;
import com.clinica.api.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByPacienteAndDataHora(Paciente paciente, LocalDateTime inicioDoDia, LocalDateTime finalDoDia);
    boolean existsByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);
}
