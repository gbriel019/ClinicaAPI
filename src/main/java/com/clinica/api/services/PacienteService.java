package com.clinica.api.services;

import com.clinica.api.entities.Paciente;
import com.clinica.api.repositories.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<Paciente> buscarTodos(){
        return pacienteRepository.findAll();
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    public Paciente salvar(Paciente paciente){
        return pacienteRepository.save(paciente);
    }

    public Paciente atualizar(Long id, Paciente pacienteAtualizado) {

        Paciente paciente = buscarPorId(id);

        paciente.setNome(pacienteAtualizado.getNome());
        paciente.setCpf(pacienteAtualizado.getCpf());
        paciente.setEmail(pacienteAtualizado.getEmail());
        paciente.setTelefone(pacienteAtualizado.getTelefone());
        paciente.setDataNascimento(pacienteAtualizado.getDataNascimento());

        return pacienteRepository.save(paciente);
    }

    public void deletar(Long id) {
        Paciente paciente = buscarPorId(id);

        paciente.setAtivo(false);

        pacienteRepository.save(paciente);
    }
}
