package com.smartgym.controller;

import com.smartgym.model.Aluno;
import com.smartgym.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alunos") // Endereço base: http://localhost:8080/alunos
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    // Rota para Cadastrar Aluno
    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE')")
    @PostMapping
    public ResponseEntity<?> criarAluno(@RequestBody Aluno aluno) {
        try {
            Aluno novoAluno = alunoService.salvar(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAluno);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Rota Multitenant para Listar Alunos de uma Academia específica
    // Exemplo: GET http://localhost:8080/alunos/academia/1
    @GetMapping("/academia/{academiaId}")
    public ResponseEntity<List<Aluno>> listarPorAcademia(@PathVariable Long academiaId) {
        return ResponseEntity.ok(alunoService.listarPorAcademia(academiaId));
    }

    // Rota para Buscar um Aluno por ID específico
    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE', 'INSTRUTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Rota para Atualizar um Aluno
    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Aluno aluno) {
        try {
            Aluno alunoAtualizado = alunoService.atualizar(id, aluno);
            return ResponseEntity.ok(alunoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Rota para Deletar Aluno
    @PreAuthorize("hasRole('GERENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            alunoService.inativar(id); // Chamando o novo método do Service
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Rota PATCH: http://localhost:8080/alunos/1/reativar
    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE')")
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<?> reativar(@PathVariable Long id) {
        try {
            alunoService.reativar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Record (DTO) para receber o JSON apenas com as senhas
    public record AlterarSenhaDTO(String senhaAtual, String novaSenha) {}

    // REGRA: Apenas quem tem a pulseira de ALUNO pode acessar esta rota
    @PreAuthorize("hasRole('ALUNO')")
    @PatchMapping("/{id}/alterar-senha")
    public ResponseEntity<?> alterarSenha(@PathVariable Long id, @RequestBody AlterarSenhaDTO senhas) {
        try {
            alunoService.alterarSenha(id, senhas.senhaAtual(), senhas.novaSenha());
            return ResponseEntity.noContent().build(); // 204: Tudo certo, senha trocada!
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}