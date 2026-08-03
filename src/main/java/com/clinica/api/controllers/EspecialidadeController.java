package com.clinica.api.controllers;

import com.clinica.api.entities.Especialidade;
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
    public ResponseEntity<List<Especialidade>> buscarTodos() {
        return ResponseEntity.ok(especialidadeService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especialidade> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Especialidade> salvar(@Valid @RequestBody Especialidade especialidade){
        Especialidade especialidadeSalvo = especialidadeService.salvar(especialidade);
        return ResponseEntity.ok(especialidadeSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Especialidade> atualizar(@PathVariable Long id, @Valid @RequestBody Especialidade especialidade) {

        Especialidade especialidadeAtualizada = especialidadeService.atualizar(id, especialidade);
        return ResponseEntity.ok(especialidadeAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especialidadeService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
