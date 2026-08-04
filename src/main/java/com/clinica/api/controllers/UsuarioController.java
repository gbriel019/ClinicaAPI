package com.clinica.api.controllers;

import com.clinica.api.entities.Usuario;
import com.clinica.api.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        return ResponseEntity.ok(usuarioService.buscarTodos());
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Usuario> salvar(@Valid @RequestBody Usuario usuario) {
        Usuario usuarioSalvo = usuarioService.salvar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id,
                                             @Valid @RequestBody Usuario usuario) {

        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
