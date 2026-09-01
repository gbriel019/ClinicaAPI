package com.clinica.api.controllers;

import com.clinica.api.dto.request.UsuarioRequest;
import com.clinica.api.dto.response.UsuarioResponse;
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
    public ResponseEntity<List<UsuarioResponse>> buscarTodos() {
        return ResponseEntity.ok(usuarioService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@Valid @RequestBody UsuarioRequest request) {

        System.out.println(">>> CHEGOU NO POST /usuarios");

        UsuarioResponse usuarioSalvo = usuarioService.salvar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody UsuarioRequest request) {

        UsuarioResponse usuarioAtualizado = usuarioService.atualizar(id, request);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}