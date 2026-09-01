package com.clinica.api.controllers;

import com.clinica.api.dto.request.EspecialidadeRequest;
import com.clinica.api.dto.response.EspecialidadeResponse;
import com.clinica.api.services.EspecialidadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    public EspecialidadeController(EspecialidadeService especialidadeService) {
        this.especialidadeService = especialidadeService;
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadeResponse>> buscarTodos() {
        return ResponseEntity.ok(especialidadeService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EspecialidadeResponse> salvar(@Valid @RequestBody EspecialidadeRequest request){
        EspecialidadeResponse especialidadeSalva = especialidadeService.salvar(request);
        return ResponseEntity.ok(especialidadeSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> atualizar(@PathVariable Long id, @Valid @RequestBody EspecialidadeRequest request) {

        EspecialidadeResponse especialidadeAtualizada = especialidadeService.atualizar(id, request);
        return ResponseEntity.ok(especialidadeAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especialidadeService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
