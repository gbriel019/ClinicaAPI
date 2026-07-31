package com.clinica.api.services;

import com.clinica.api.entities.Especialidade;
import com.clinica.api.repositories.EspecialidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    public EspecialidadeService(EspecialidadeRepository especialidadeRepository) {
        this.especialidadeRepository = especialidadeRepository;
    }

    public List<Especialidade> buscarTodos(){
        return especialidadeRepository.findAll();
    }

    public Especialidade buscarPorId(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
    }

    public Especialidade salvar(Especialidade especialidade) {

        if (especialidadeRepository.findByNome(especialidade.getNome()).isPresent()) {
            throw new RuntimeException("Já existe uma especialidade com esse nome");
        }
        return especialidadeRepository.save(especialidade);
    }

    public Especialidade atualizar(Long id, Especialidade especialidadeAtualizada) {
        Especialidade especialidade = buscarPorId(id);

        especialidade.setNome(especialidadeAtualizada.getNome());

        return especialidadeRepository.save(especialidade);
    }

    public void deletar(Long id){
        Especialidade especialidade = buscarPorId(id);
        especialidadeRepository.delete(especialidade);
    }
}
