package com.smartgym.controller;

import com.smartgym.model.Exercicio;
import com.smartgym.service.ExercicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/exercicios")
public class ExercicioController {

    private final ExercicioService exercicioService;

    public ExercicioController(ExercicioService exercicioService) {
        this.exercicioService = exercicioService;
    }
    @PreAuthorize("hasAnyRole('GERENTE', 'INSTRUTOR')")
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Exercicio exercicio) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(exercicioService.salvar(exercicio));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/academia/{academiaId}")
    public ResponseEntity<List<Exercicio>> listarPorAcademia(@PathVariable Long academiaId) {
        return ResponseEntity.ok(exercicioService.listarPorAcademia(academiaId));
    }

    // NOVO: Rota para Editar
    @PreAuthorize("hasAnyRole('GERENTE', 'INSTRUTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Exercicio exercicio) {
        try {
            Exercicio atualizado = exercicioService.atualizar(id, exercicio);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // NOVO: Rota para Excluir (Inativar)
    @PreAuthorize("hasAnyRole('GERENTE', 'INSTRUTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            exercicioService.inativar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}