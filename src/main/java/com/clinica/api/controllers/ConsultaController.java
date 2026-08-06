package com.clinica.api.controllers;

import com.clinica.api.dto.request.CancelarConsultaRequest;
import com.clinica.api.dto.request.ConsultaRequest;
import com.clinica.api.dto.response.ConsultaResponse;
import com.clinica.api.services.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
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
    public ResponseEntity<List<ConsultaResponse>> buscarTodos() {
        return ResponseEntity.ok(consultaService.buscarTodos());
    }

    @GetMapping("/{id}")
    @Cacheable
    public ResponseEntity<ConsultaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ConsultaResponse> salvar(@Valid @RequestBody ConsultaRequest request){
        ConsultaResponse consultaSalva = consultaService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaSalva);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ConsultaResponse> cancelar(@PathVariable Long id, @RequestParam CancelarConsultaRequest request) {

        ConsultaResponse consultaCancelada = consultaService.cancelar(id, request);
        return ResponseEntity.ok(consultaCancelada);
    }

}