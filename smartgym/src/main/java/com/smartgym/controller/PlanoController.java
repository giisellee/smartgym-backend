package com.smartgym.controller;

import com.smartgym.model.Plano;
import com.smartgym.service.PlanoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos") // Endereço base: http://localhost:8080/planos
public class PlanoController {

    private final PlanoService planoService;

    public PlanoController(PlanoService planoService) {
        this.planoService = planoService;
    }

    // Rota: POST http://localhost:8080/planos
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Plano plano) {
        try {
            Plano novoPlano = planoService.salvarPlano(plano);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPlano);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ROTA GET PROTEGIDA: O Front-end TEM que avisar de qual academia quer ver os planos
    // Exemplo de uso: GET http://localhost:8080/planos/academia/1
    @GetMapping("/academia/{academiaId}")
    public ResponseEntity<List<Plano>> listarPorAcademia(@PathVariable Long academiaId) {
        List<Plano> planos = planoService.listarPorAcademia(academiaId);
        return ResponseEntity.ok(planos);
    }

    // Rota: PUT http://localhost:8080/planos/5
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Plano plano) {
        try {
            Plano planoAtualizado = planoService.atualizarPlano(id, plano);
            return ResponseEntity.ok(planoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Rota: DELETE http://localhost:8080/planos/5
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            planoService.inativarPlano(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}