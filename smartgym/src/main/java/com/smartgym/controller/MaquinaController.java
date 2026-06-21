package com.smartgym.controller;

import com.smartgym.model.Maquina;
import com.smartgym.service.MaquinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maquinas") // Endereço base: http://localhost:8080/maquinas
public class MaquinaController {

    private final MaquinaService maquinaService;

    // Injeção de dependência via Construtor
    public MaquinaController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    // --- ROTA POST: CADASTRAR NOVO EQUIPAMENTO ---
    @PreAuthorize("hasAnyRole('GERENTE', 'INSTRUTOR')")
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Maquina maquina) {
        try {
            Maquina novaMaquina = maquinaService.salvarMaquina(maquina);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaMaquina);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // --- ROTA GET PROTEGIDA: LISTAR EQUIPAMENTOS DE UMA ACADEMIA ---
    // Exemplo: GET http://localhost:8080/maquinas/academia/1
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/academia/{academiaId}")
    public ResponseEntity<List<Maquina>> listarPorAcademia(@PathVariable Long academiaId) {
        List<Maquina> maquinas = maquinaService.listarPorAcademia(academiaId);
        return ResponseEntity.ok(maquinas);
    }

    // --- ROTA PUT: EDITAR INFORMAÇÕES DO EQUIPAMENTO ---
    @PreAuthorize("hasAnyRole('GERENTE', 'INSTRUTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Maquina maquina) {
        try {
            Maquina maquinaAtualizada = maquinaService.atualizarMaquina(id, maquina);
            return ResponseEntity.ok(maquinaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // --- ROTA PATCH: MARCAR COMO AVARIADA/MANUTENÇÃO ---
    @PreAuthorize("hasAnyRole('GERENTE', 'INSTRUTOR')")
    @PatchMapping("/{id}/manutencao")
    public ResponseEntity<?> marcarManutencao(@PathVariable Long id) {
        try {
            maquinaService.marcarManutencao(id);
            return ResponseEntity.noContent().build(); // 204: Sucesso, sem conteúdo para retornar
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // --- ROTA PATCH: MARCAR COMO FUNCIONAL ---
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<?> ativarMaquina(@PathVariable Long id) {
        try {
            maquinaService.ativarMaquina(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}