package com.clinica.api.controllers;

import com.clinica.api.dto.request.MedicoRequest;
import com.clinica.api.dto.response.MedicoResponse;
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
    public ResponseEntity<List<MedicoResponse>> buscarTodos(){
        return ResponseEntity.ok(medicoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MedicoResponse> salvar(@Valid @RequestBody MedicoRequest request){
        MedicoResponse medicoSalvo = medicoService.salvar(request);
        return ResponseEntity.ok(medicoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody MedicoRequest request){

        MedicoResponse medicoAtualizado = medicoService.atualizar(id, request);
        return ResponseEntity.ok(medicoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        medicoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
