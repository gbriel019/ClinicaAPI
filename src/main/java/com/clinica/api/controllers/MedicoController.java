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
    public List<Medico> buscarTodos(){
        return medicoService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Medico buscarPorId(@PathVariable Long id) {
        return medicoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Medico> salvar(@Valid @RequestBody Medico medico){
        Medico medicoSalvo = medicoService.salvar(medico);
        return ResponseEntity.ok(medicoSalvo);
    }

    @PutMapping("/{id}")
    public Medico atualizar(@PathVariable Long id, @Valid @RequestBody Medico medico){
        return medicoService.atualizar(id, medico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        medicoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
