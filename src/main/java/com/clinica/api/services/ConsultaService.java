package com.clinica.api.services;

import java.time.DayOfWeek;

import com.clinica.api.dto.request.CancelarConsultaRequest;
import com.clinica.api.dto.request.ConsultaRequest;
import com.clinica.api.dto.response.ConsultaResponse;
import com.clinica.api.entities.Consulta;
import com.clinica.api.entities.Medico;
import com.clinica.api.entities.Paciente;
import com.clinica.api.enums.StatusConsulta;
import com.clinica.api.exception.NotFound;
import com.clinica.api.repositories.ConsultaRepository;
import com.clinica.api.repositories.MedicoRepository;
import com.clinica.api.repositories.PacienteRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
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


    private ConsultaResponse converterParaResponse(Consulta consulta) {
        ConsultaResponse response = new ConsultaResponse();
        response.setId(consulta.getId());
        response.setMedicoId(consulta.getMedico().getId());
        response.setNomeMedico(consulta.getMedico().getNome());
        response.setPacienteId(consulta.getPaciente().getId());
        response.setNomePaciente(consulta.getPaciente().getNome());
        response.setDataHora(consulta.getDataHora());
        response.setStatus(consulta.getStatus());
        response.setMotivoCancelamento(consulta.getMotivoCancelamento());

        return response;
    }

    @Cacheable("consultas")
    public List<ConsultaResponse> buscarTodos(){
        return consultaRepository.findAll().stream().map(this::converterParaResponse).toList();
    }

    @Cacheable(value = "consulta", key = "#id")
    public ConsultaResponse buscarPorId(Long id){
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new NotFound("Consulta não encontrada"));

        return converterParaResponse(consulta);
    }

    @CacheEvict(value = {"consultas" , "consulta"}, allEntries = true)
    public ConsultaResponse salvar(ConsultaRequest request) {
        Medico medico = medicoRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new NotFound("Médico não encontrado"));

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new NotFound("Paciente não encontrado"));

        if (!medico.getAtivo()) {
            throw new RuntimeException("Não é possível agendar consulta para um médico inativo");
        }

        if (!paciente.getAtivo()) {
            throw new RuntimeException(("Não é possivel agendar uma consulta com o paciente inativo"));
        }

        int hora = request.getDataHora().getHour();
        if (hora <7 || hora >=19){
            throw new RuntimeException("Horário indisponivel para Agendamento");
        }

        DayOfWeek diaSemana = request.getDataHora().getDayOfWeek();
        if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
            throw new RuntimeException("Dia indisponivel para Agendamento");
        }

        if (consultaRepository.existsByMedicoAndDataHora(medico, request.getDataHora())) {
            throw new RuntimeException("O médico já possui uma consulta neste horário");
        }

        LocalDateTime inicioDoDia = request.getDataHora().toLocalDate().atStartOfDay();
        LocalDateTime finalDoDia = request.getDataHora().toLocalDate().atTime(23, 59, 59);

        if (consultaRepository.existsByPacienteAndDataHoraBetween(paciente, inicioDoDia, finalDoDia)) {
            throw new RuntimeException("O paciente já possui uma consulta Agendada neste dia");
        }

        Consulta consulta = new Consulta();
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setDataHora(request.getDataHora());
        consulta.setStatus(StatusConsulta.AGENDADA);

        Consulta consultaSalva = consultaRepository.save(consulta);
        return converterParaResponse(consultaSalva);
    }

    @CacheEvict(value = {"especialidades", "especialidade"}, allEntries = true)
    public ConsultaResponse cancelar(Long id, CancelarConsultaRequest request) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        LocalDateTime agora = LocalDateTime.now();
        long horas = Duration.between(agora, consulta.getDataHora()).toHours();

        if (horas <24){
            throw new RuntimeException("A consulta so pode ser cancelada com 24 horas de antecedencia");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        consulta.setMotivoCancelamento(request.getMotivo());

        return converterParaResponse(consultaRepository.save(consulta));
    }


}