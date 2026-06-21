package com.smartgym.controller;

import com.smartgym.model.Pagamento;
import com.smartgym.service.PagamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) { this.pagamentoService = pagamentoService; }
    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE')")
    @PostMapping("/gerar")
    public ResponseEntity<?> gerarCobranca(@RequestBody Pagamento pagamento) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.gerarCobranca(pagamento));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE')")
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarPagamento(@PathVariable Long id) {
        try {
            pagamentoService.confirmarPagamento(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE')")
    @GetMapping("/academia/{academiaId}")
    public ResponseEntity<List<Pagamento>> verCaixa(@PathVariable Long academiaId) {
        return ResponseEntity.ok(pagamentoService.listarCaixaDaAcademia(academiaId));
    }

    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<Pagamento>> verFaturas(@PathVariable Long alunoId) {
        return ResponseEntity.ok(pagamentoService.listarFaturasDoAluno(alunoId));
    }
}