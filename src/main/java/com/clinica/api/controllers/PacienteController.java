package com.clinica.api.controllers;

import com.clinica.api.entities.Paciente;
import com.clinica.api.services.PacienteService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<Paciente>> buscarTodos(){
        return ResponseEntity.ok(pacienteService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<Paciente> salvar(@Valid @RequestBody Paciente paciente) {
        Paciente pacienteSalvo = pacienteService.salvar(paciente);
        return ResponseEntity.ok(pacienteSalvo);
    }
}
