package com.clinica.api.services;

import java.time.DayOfWeek;

import com.clinica.api.config.mappers.ConsultaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ConsultaMapper consultaMapper;

    public ConsultaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository, ConsultaMapper consultaMapper) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.consultaMapper = consultaMapper;
    }

    private static final Logger log = LoggerFactory.getLogger(ConsultaService.class);


    @Cacheable("consultas")
    public List<ConsultaResponse> buscarTodos() {
        log.info("Buscando todas as consultas");
        return consultaRepository.findAll().stream().map(consultaMapper::toResponse).toList();
    }

    @Cacheable(value = "consulta", key = "#id")
    public ConsultaResponse buscarPorId(Long id) {
        log.info("Buscando consulta com ID {}", id);
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new NotFound("Consulta não encontrada"));

        return consultaMapper.toResponse(consulta);
    }

    @CacheEvict(value = {"consultas", "consulta"}, allEntries = true)
    public ConsultaResponse salvar(ConsultaRequest request) {
        log.info("Cadastrando nova consulta para o médico {} e paciente {}",
                request.getMedicoId(), request.getPacienteId());
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

        if (request.getDataHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível agendar uma consulta em uma data/horário no passado");
        }

            int hora = request.getDataHora().getHour();
            if (hora < 7 || hora >= 19) {
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

            Consulta consulta = consultaMapper.toEntity(request);
            consulta.setMedico(medico);
            consulta.setPaciente(paciente);
            consulta.setStatus(StatusConsulta.AGENDADA);

            Consulta consultaSalva = consultaRepository.save(consulta);
            return consultaMapper.toResponse(consultaSalva);
        }

        @CacheEvict(value = {"consultas", "consulta"}, allEntries = true)
        public ConsultaResponse cancelar (Long id, CancelarConsultaRequest request){
            log.info("Cancelando consulta com ID {}", id);
            Consulta consulta = consultaRepository.findById(id)
                    .orElseThrow(() -> new NotFound("Consulta não encontrada"));

            LocalDateTime agora = LocalDateTime.now();
            long horas = Duration.between(agora, consulta.getDataHora()).toHours();

            if (horas < 24) {
                throw new RuntimeException("A consulta so pode ser cancelada com 24 horas de antecedencia");
            }

            consulta.setStatus(StatusConsulta.CANCELADA);
            consulta.setMotivoCancelamento(request.getMotivo());

            return consultaMapper.toResponse(consultaRepository.save(consulta));

        }


    }
