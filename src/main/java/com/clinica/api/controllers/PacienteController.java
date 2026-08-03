package com.clinica.api.controllers;

import com.clinica.api.entities.Paciente;
import com.clinica.api.services.PacienteService;
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
    public List<Paciente> buscarTodos(){
        return pacienteService.buscarTodos();
    }

    @PostMapping
    public Paciente salvar(@RequestBody Paciente paciente) {
        return pacienteService.salvar(paciente);
    }
}
