package com.clinica.api.services;

import com.clinica.api.dto.request.EspecialidadeRequest;
import com.clinica.api.dto.response.EspecialidadeResponse;
import com.clinica.api.entities.Especialidade;
import com.clinica.api.repositories.EspecialidadeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable("especialidades")
    public List<EspecialidadeResponse> buscarTodos(){
        return especialidadeRepository.findAll().stream().map(this::converterParaResponse).toList();
    }

    @Cacheable(value = "especialidade", key = "#id")
    public EspecialidadeResponse buscarPorId(Long id) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
        return converterParaResponse(especialidade);
    }

    @CacheEvict(value = {"especialidades", "especialidade"}, allEntries = true)
    public EspecialidadeResponse salvar(EspecialidadeRequest request) {
        Especialidade especialidade = new Especialidade();
        especialidade.setNome(request.getNome());
        if (especialidadeRepository.findByNome(especialidade.getNome()).isPresent()) {
            throw new RuntimeException("Já existe uma especialidade com esse nome");
        }
        Especialidade especialidadeSalva = especialidadeRepository.save(especialidade);

        return converterParaResponse(especialidadeSalva);
    }

    @CacheEvict(value = {"especialidades", "especialidade"}, allEntries = true)
    public EspecialidadeResponse atualizar(Long id, EspecialidadeRequest request) {

        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));

        especialidade.setNome(request.getNome());

        Especialidade especialidadeAtualizada = especialidadeRepository.save(especialidade);

        return converterParaResponse(especialidadeAtualizada);
    }

    @CacheEvict(value = {"especialidades", "especialidade"}, allEntries = true)
    public void deletar(Long id){
        Especialidade especialidade = especialidadeRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
        especialidadeRepository.delete(especialidade);
    }
}
