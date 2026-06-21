package com.smartgym.controller;

import com.smartgym.model.Treino;
import com.smartgym.service.TreinoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/treinos")
public class TreinoController {

    private final TreinoService treinoService;

    public TreinoController(TreinoService treinoService) {
        this.treinoService = treinoService;
    }

    @PreAuthorize("hasAnyRole('GERENTE','INSTRUTOR')")
    @PostMapping
    public ResponseEntity<?> criarTreino(@RequestBody Treino treino) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(treinoService.salvarTreino(treino));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // O Frontend envia o ID do aluno e o ID da academia para listar os treinos
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/aluno/{alunoId}/academia/{academiaId}")
    public ResponseEntity<List<Treino>> listarTreinosDoAluno(@PathVariable Long alunoId, @PathVariable Long academiaId) {
        return ResponseEntity.ok(treinoService.buscarTreinosDoAluno(alunoId, academiaId));
    }

    // Rota para marcar o treino como "Feito!"
    @PreAuthorize("hasRole('ALUNO')")
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<?> concluirTreino(@PathVariable Long id) {
        try {
            treinoService.concluirTreino(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}