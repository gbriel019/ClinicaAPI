package com.clinica.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.clinica.api.dto.request.UsuarioRequest;
import com.clinica.api.dto.response.UsuarioResponse;
import com.clinica.api.entities.Usuario;
import com.clinica.api.repositories.UsuarioRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;


    private final UsuarioRepository usuarioRepository;

    public UsuarioService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private UsuarioResponse converterParaResponse(Usuario usuario) {

        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setRole(usuario.getRole());
        response.setAtivo(usuario.getAtivo());


        return response;
    }

    @Cacheable("usuarios")
    public List<UsuarioResponse> buscarTodos(){
        log.info("Buscando todos os usuarios");
        return usuarioRepository.findAll().stream().map(this::converterParaResponse).toList();
    }

    @Cacheable(value = "usuario", key = "#id")
    public UsuarioResponse buscarPorId(Long id) {
        log.info("Buscando usuario com ID {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrada"));

        return converterParaResponse(usuario);
    }

    @CacheEvict(value = {"usuarios", "usuario"}, allEntries = true)
    public UsuarioResponse salvar(UsuarioRequest request) {
        log.info("Salvando usuario com email {}", request.getEmail());

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Já existe um usuário com este e-mail");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));

        usuario.setRole(request.getRole());
        usuario.setAtivo(request.getAtivo());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return converterParaResponse(usuarioSalvo);
    }

    @CacheEvict(value = {"usuarios", "usuario"}, allEntries = true)
    public UsuarioResponse atualizar(Long id, UsuarioRequest request){
        log.info("Atualizando usuario com ID {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setRole(request.getRole());
        usuario.setAtivo(request.getAtivo());


        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return converterParaResponse(usuarioAtualizado);
    }

    @CacheEvict(value = {"usuarios", "usuario"}, allEntries = true)
    public void deletar(Long id){
        log.info("Deletando usuario com ID {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuáio não encontrado"));

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);
    }
}