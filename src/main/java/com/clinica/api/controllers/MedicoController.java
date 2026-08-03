package com.clinica.api.controllers;

import com.clinica.api.entities.Medico;
import com.clinica.api.services.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public ResponseEntity<List<Medico>> buscarTodos(){
        return ResponseEntity.ok(medicoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Medico> salvar(@Valid @RequestBody Medico medico){
        Medico medicoSalvo = medicoService.salvar(medico);
        return ResponseEntity.ok(medicoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medico> atualizar(@PathVariable Long id, @Valid @RequestBody Medico medico){

        Medico medicoAtualizado = medicoService.atualizar(id, medico);
        return ResponseEntity.ok(medicoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        medicoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
