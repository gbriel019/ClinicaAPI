package com.clinica.api.services;

import com.clinica.api.entities.Especialidade;
import com.clinica.api.entities.Medico;
import com.clinica.api.repositories.EspecialidadeRepository;
import com.clinica.api.repositories.MedicoRepository;
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

    public List<Medico> buscarTodos(){
        return medicoRepository.findAll();
    }

    public Medico buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
    }

    public Medico salvar(Medico medico) {
        Especialidade especialidade = especialidadeRepository
                .findById(medico.getEspecialidade().getId())
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));

        medico.setEspecialidade(especialidade);
        return medicoRepository.save(medico);
    }

    public Medico atualizar(Long id, Medico medicoAtualizado) {
        Medico medico = buscarPorId(id);

        Especialidade especialidade = especialidadeRepository
                .findById(medicoAtualizado.getEspecialidade().getId())
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));

        medico.setNome(medicoAtualizado.getNome());
        medico.setCrm(medicoAtualizado.getCrm());
        medico.setEmail(medicoAtualizado.getEmail());
        medico.setTelefone(medicoAtualizado.getTelefone());
        medico.setEspecialidade(especialidade);

        return medicoRepository.save(medico);
    }

    public void deletar(Long id) {
        Medico medico = buscarPorId(id);

        medico.setAtivo(false);

        medicoRepository.save(medico);
    }
}
