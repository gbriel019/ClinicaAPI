package com.clinica.api.services;

import com.clinica.api.entities.Especialidade;
import com.clinica.api.entities.Usuario;
import com.clinica.api.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServices {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServices(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> buscarTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrada"));
    }
}
