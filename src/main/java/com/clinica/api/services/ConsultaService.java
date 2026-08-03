package com.clinica.api.services;

import java.time.DayOfWeek;
import com.clinica.api.entities.Consulta;
import com.clinica.api.entities.Medico;
import com.clinica.api.entities.Paciente;
import com.clinica.api.enums.StatusConsulta;
import com.clinica.api.exception.NotFound;
import com.clinica.api.repositories.ConsultaRepository;
import com.clinica.api.repositories.MedicoRepository;
import com.clinica.api.repositories.PacienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public List<Consulta> buscarTodos(){
        return consultaRepository.findAll();
    }

    public Consulta buscarPorId(Long id){
        return consultaRepository.findById(id)
                .orElseThrow(() -> new NotFound("Consulta não encontrada"));
    }

    public Consulta salvar(Consulta consulta) {
        Medico medico = medicoRepository.findById(consulta.getMedico().getId())
                .orElseThrow(() -> new NotFound("Médico não encontrado"));

        Paciente paciente = pacienteRepository.findById(consulta.getPaciente().getId())
                .orElseThrow(() -> new NotFound("Paciente não encontrado"));

        if (!medico.getAtivo()) {
            throw new RuntimeException("Não é possível agendar consulta para um médico inativo");
        }

        if (!paciente.getAtivo()) {
            throw new RuntimeException(("Não é possivel agendar uma consulta com o paciente inativo"));
        }

        int hora = consulta.getDataHora().getHour();
        if (hora <7 || hora >=19){
            throw new RuntimeException("Horário indisponivel para Agendamento");
        }

        DayOfWeek diaSemana = consulta.getDataHora().getDayOfWeek();
        if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
            throw new RuntimeException("Dia indisponivel para Agendamento");
        }

        if (consultaRepository.existsByMedicoAndDataHora(medico, consulta.getDataHora())) {
            throw new RuntimeException("O médico já possui uma consulta neste horário");
        }

        LocalDateTime inicioDoDia = consulta.getDataHora().toLocalDate().atStartOfDay();
        LocalDateTime finalDoDia = consulta.getDataHora().toLocalDate().atTime(23, 59, 59);

        if (consultaRepository.existsByPacienteAndDataHora(paciente, inicioDoDia, finalDoDia)) {
            throw new RuntimeException("O paciente já possui uma consulta Agendada neste dia");
        }

        consulta.setStatus(StatusConsulta.AGENDADA);
        return consultaRepository.save(consulta);
    }
}
