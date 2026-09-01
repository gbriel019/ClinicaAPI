package com.clinica.api.services;

import com.clinica.api.config.mappers.EspecialidadeMapper;
import com.clinica.api.exception.ConflictException;
import com.clinica.api.exception.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final EspecialidadeMapper especialidadeMapper;

    public EspecialidadeService(EspecialidadeRepository especialidadeRepository, EspecialidadeMapper especialidadeMapper) {
        this.especialidadeRepository = especialidadeRepository;
        this.especialidadeMapper = especialidadeMapper;
    }

    private static final Logger log = LoggerFactory.getLogger(EspecialidadeService.class);

    @Cacheable("especialidades")
    public List<EspecialidadeResponse> buscarTodos(){
        log.info("Buscando todas as especialidades");
        return especialidadeRepository.findAll().stream().map(especialidadeMapper::toResponse).toList();
    }

    @Cacheable(value = "especialidade", key = "#id")
    public EspecialidadeResponse buscarPorId(Long id) {
        log.info("Buscando especialidade com ID {}", id);
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new NotFound("Especialidade não encontrada"));
        return especialidadeMapper.toResponse(especialidade);
    }

    @CacheEvict(value = {"especialidades", "especialidade"}, allEntries = true)
    public EspecialidadeResponse salvar(EspecialidadeRequest request) {
        log.info("Cadastrando nova especialidade {}", request.getNome());
        Especialidade especialidade = especialidadeMapper.toEntity(request);
        if (especialidadeRepository.findByNome(especialidade.getNome()).isPresent()) {
            throw new ConflictException("Já existe uma especialidade com esse nome");
        }
        Especialidade especialidadeSalva = especialidadeRepository.save(especialidade);

        return especialidadeMapper.toResponse(especialidadeSalva);
    }

    @CacheEvict(value = {"especialidades", "especialidade"}, allEntries = true)
    public EspecialidadeResponse atualizar(Long id, EspecialidadeRequest request) {
        log.info("Atualizando especialidade {}", id);

        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new NotFound("Especialidade não encontrada"));

        especialidade.setNome(request.getNome());

        Especialidade especialidadeAtualizada = especialidadeRepository.save(especialidade);

        return especialidadeMapper.toResponse(especialidadeAtualizada);
    }

    @CacheEvict(value = {"especialidades", "especialidade"}, allEntries = true)
    public void deletar(Long id){
        log.info("Deletando especialidade com o ID {}", id);
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new NotFound("Especialidade não encontrada"));
        especialidadeRepository.delete(especialidade);
    }
}