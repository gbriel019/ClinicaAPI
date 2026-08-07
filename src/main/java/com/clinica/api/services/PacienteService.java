package com.clinica.api.services;

import com.clinica.api.dto.request.PacienteRequest;
import com.clinica.api.dto.response.PacienteResponse;
import com.clinica.api.entities.Paciente;
import com.clinica.api.repositories.PacienteRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    private PacienteResponse converterParaResponse(Paciente paciente) {


        PacienteResponse response = new PacienteResponse();
        response.setId(paciente.getId());
        response.setNome(paciente.getNome());
        response.setCpf(paciente.getCpf());
        response.setEmail(paciente.getEmail());
        response.setTelefone(paciente.getTelefone());
        response.setDataNascimento(paciente.getDataNascimento());
        response.setAtivo(paciente.getAtivo());

        return response;
    }

    @Cacheable("pacientes")
    public List<PacienteResponse> buscarTodos(){
        return pacienteRepository.findAll().stream().map(this::converterParaResponse).toList();
    }

    @Cacheable(value = "paciente", key = "#id")
    public PacienteResponse buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        return converterParaResponse(paciente);
    }

    @CacheEvict(value = {"pacientes", "paciente"}, allEntries = true)
    public PacienteResponse salvar(PacienteRequest request) {

        if (pacienteRepository.findByCpf(request.getCpf()).isPresent()) {
            throw new RuntimeException("Já existe um paciente com esse CPF");
        }

        if (pacienteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Já existe um paciente com esse e-mail");
        }

        Paciente paciente = new Paciente();
        paciente.setNome(request.getNome());
        paciente.setCpf(request.getCpf());
        paciente.setEmail(request.getEmail());
        paciente.setTelefone(request.getTelefone());
        paciente.setDataNascimento(request.getDataNascimento());



        Paciente pacienteSalvo = pacienteRepository.save(paciente);

        return converterParaResponse(pacienteSalvo);
    }

    @CacheEvict(value = {"pacientes", "paciente"}, allEntries = true)
    public PacienteResponse atualizar(Long id, PacienteRequest request) {

        Paciente paciente = pacienteRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        paciente.setNome(request.getNome());
        paciente.setCpf(request.getCpf());
        paciente.setEmail(request.getEmail());
        paciente.setTelefone(request.getTelefone());
        paciente.setDataNascimento(request.getDataNascimento());

        Paciente pacienteAtualizado = pacienteRepository.save(paciente);

        return converterParaResponse(pacienteAtualizado);
    }

    @CacheEvict(value = {"pacientes", "paciente"}, allEntries = true)
    public void deletar(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        paciente.setAtivo(false);

        pacienteRepository.save(paciente);
    }
}
