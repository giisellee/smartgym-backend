package com.smartgym.controller;

import com.smartgym.model.AvaliacaoFisica;
import com.smartgym.service.AvaliacaoFisicaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoFisicaController {
    private final AvaliacaoFisicaService avaliacaoService;

    public AvaliacaoFisicaController(AvaliacaoFisicaService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'INSTRUTOR')")
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody AvaliacaoFisica avaliacao) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoService.registrarAvaliacao(avaliacao));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<AvaliacaoFisica>> verEvolucao(@PathVariable Long alunoId) {
        return ResponseEntity.ok(avaliacaoService.listarEvolucaoDoAluno(alunoId));
    }
}