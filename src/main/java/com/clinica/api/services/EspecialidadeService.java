package com.clinica.api.services;

import com.clinica.api.dto.request.EspecialidadeRequest;
import com.clinica.api.dto.response.EspecialidadeResponse;
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

    private EspecialidadeResponse converterParaResponse(Especialidade especialidade) {
        EspecialidadeResponse response = new EspecialidadeResponse();
        response.setId(especialidade.getId());
        response.setNome(especialidade.getNome());

        return response;
    }

    public List<EspecialidadeResponse> buscarTodos(){
        return especialidadeRepository.findAll().stream().map(this::converterParaResponse).toList();
    }

    public EspecialidadeResponse buscarPorId(Long id) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
        return converterParaResponse(especialidade);
    }

    public EspecialidadeResponse salvar(EspecialidadeRequest request) {
        Especialidade especialidade = new Especialidade();
        especialidade.setNome(request.getNome());
        if (especialidadeRepository.findByNome(especialidade.getNome()).isPresent()) {
            throw new RuntimeException("Já existe uma especialidade com esse nome");
        }
        Especialidade especialidadeSalva = especialidadeRepository.save(especialidade);

        return converterParaResponse(especialidadeSalva);
    }

    public EspecialidadeResponse atualizar(Long id, EspecialidadeRequest request) {

        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));

        especialidade.setNome(request.getNome());

        Especialidade especialidadeAtualizada = especialidadeRepository.save(especialidade);

        return converterParaResponse(especialidadeAtualizada);
    }

    public void deletar(Long id){
        Especialidade especialidade = especialidadeRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
        especialidadeRepository.delete(especialidade);
    }
}
