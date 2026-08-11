package com.clinica.api.services;

import com.clinica.api.exception.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.clinica.api.dto.request.MedicoRequest;
import com.clinica.api.dto.response.EspecialidadeResponse;
import com.clinica.api.dto.response.MedicoResponse;
import com.clinica.api.entities.Especialidade;
import com.clinica.api.entities.Medico;
import com.clinica.api.repositories.EspecialidadeRepository;
import com.clinica.api.repositories.MedicoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadeRepository especialidadeRepository;

    public MedicoService(MedicoRepository medicoRepository, EspecialidadeRepository especialidadeRepository) {
        this.medicoRepository = medicoRepository;
        this.especialidadeRepository = especialidadeRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(MedicoService.class);

    private MedicoResponse converterParaResponse(Medico medico) {
        EspecialidadeResponse especialidadeResponse = new EspecialidadeResponse();
        especialidadeResponse.setId(medico.getEspecialidade().getId());
        especialidadeResponse.setNome(medico.getEspecialidade().getNome());

        MedicoResponse response = new MedicoResponse();
        response.setId(medico.getId());
        response.setNome(medico.getNome());
        response.setCrm(medico.getCrm());
        response.setEmail(medico.getEmail());
        response.setTelefone(medico.getTelefone());
        response.setEspecialidade(especialidadeResponse);
        response.setAtivo(medico.getAtivo());

        return response;
    }

    @Cacheable("medicos")
    public List<MedicoResponse> buscarTodos(){
        log.info("Buscando todos os medicos");
        return medicoRepository.findAll().stream().map(this::converterParaResponse).toList();
    }

    @Cacheable(value = "medico", key = "#id")
    public MedicoResponse buscarPorId(Long id) {
        log.info("Buscando medico com ID {}", id);

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new NotFound("Médico não encontrado"));

        return converterParaResponse(medico);
    }

    @CacheEvict(value = {"medicos", "medico"}, allEntries = true)
    public MedicoResponse salvar(MedicoRequest request) {
        log.info("Cadastrando novo medico com CRM {}", request.getCrm());
        Especialidade especialidade = especialidadeRepository
                .findById(request.getEspecialidadeId())
                .orElseThrow(() -> new NotFound("Especialidade não encontrada"));

        Medico medico = new Medico();
        medico.setNome(request.getNome());
        medico.setCrm(request.getCrm());
        medico.setEmail(request.getEmail());
        medico.setTelefone(request.getTelefone());
        medico.setEspecialidade(especialidade);

        Medico medicoSalvo = medicoRepository.save(medico);

        return converterParaResponse(medicoSalvo);
    }

    @CacheEvict(value = {"medicos", "medico"}, allEntries = true)
    public MedicoResponse atualizar(Long id, MedicoRequest request) {
        log.info("Atualizando medico com o ID {}", id);
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new NotFound("Medico não encontrado"));

        Especialidade especialidade = especialidadeRepository
                .findById(request.getEspecialidadeId())
                .orElseThrow(() -> new NotFound("Especialidade não encontrada"));


        medico.setNome(request.getNome());
        medico.setCrm(request.getCrm());
        medico.setEmail(request.getEmail());
        medico.setTelefone(request.getTelefone());
        medico.setEspecialidade(especialidade);

        Medico medicoAtualizado = medicoRepository.save(medico);
        return converterParaResponse(medicoAtualizado);
    }

    @CacheEvict(value = {"medicos", "medico"}, allEntries = true)
    public void deletar(Long id) {
        log.info("Deletando medico com o ID {}", id);
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new NotFound("Medico não encontrado"));

        medico.setAtivo(false);

        medicoRepository.save(medico);
    }
}
