package com.clinica.api.controllers;

import com.clinica.api.dto.request.PacienteRequest;
import com.clinica.api.dto.response.PacienteResponse;
import com.clinica.api.services.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponse>> buscarTodos(){
        return ResponseEntity.ok(pacienteService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public List<PacienteResponse> buscarPorNome(
            @RequestParam String nome) {
        return pacienteService.buscarPorNome(nome);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> atualizar(@PathVariable Long id,
                                              @Valid @RequestBody PacienteRequest request) {
        PacienteResponse pacienteAtualizado = pacienteService.atualizar(id, request);
        return ResponseEntity.ok(pacienteAtualizado);
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> salvar(@Valid @RequestBody PacienteRequest request) {
        PacienteResponse pacienteSalvo = pacienteService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
