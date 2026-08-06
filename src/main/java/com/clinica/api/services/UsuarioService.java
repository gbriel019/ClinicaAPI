package com.clinica.api.services;

import com.clinica.api.dto.request.UsuarioRequest;
import com.clinica.api.dto.response.UsuarioResponse;
import com.clinica.api.entities.Usuario;
import com.clinica.api.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private UsuarioResponse converterParaResponse(Usuario usuario) {

        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setRole(usuario.getRole());
        response.setAtivo(usuario.getAtivo());

        return response;
    }

    public List<UsuarioResponse> buscarTodos(){
        return usuarioRepository.findAll().stream().map(this::converterParaResponse).toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrada"));

        return converterParaResponse(usuario);
    }

    public UsuarioResponse salvar(UsuarioRequest request){

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Já existe um usuário com este e-mail");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setRole(request.getRole());
        usuario.setAtivo(request.getAtivo());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return converterParaResponse(usuarioSalvo);
    }

    public UsuarioResponse atualizar(Long id, UsuarioRequest request){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setRole(request.getRole());
        usuario.setAtivo(request.getAtivo());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return converterParaResponse(usuarioAtualizado);
    }

    public void deletar(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuáio não encontrado"));

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);
    }
}