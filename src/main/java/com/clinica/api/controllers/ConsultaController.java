package com.clinica.api.controllers;

import com.clinica.api.entities.Consulta;
import com.clinica.api.services.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> buscarTodos() {
        return ResponseEntity.ok(consultaService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Consulta> salvar(@Valid @RequestBody Consulta consulta){
        Consulta consultaSalva = consultaService.salvar(consulta);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaSalva);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Consulta> cancelar(@PathVariable Long id, @RequestParam String motivo) {

        Consulta consultaCancelada = consultaService.cancelar(id, motivo);
        return ResponseEntity.ok(consultaCancelada);
    }

}
