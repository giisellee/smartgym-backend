package com.smartgym.controller;

import com.smartgym.model.Aviso;
import com.smartgym.service.AvisoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/avisos")
public class AvisoController {
    private final AvisoService avisoService;

    public AvisoController(AvisoService avisoService) { this.avisoService = avisoService; }

    //REGRA: Só Gerentes e Atendentes podem publicar avisos no mural
    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE')")
    @PostMapping
    public ResponseEntity<?> publicar(@RequestBody Aviso aviso) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(avisoService.publicarAviso(aviso));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // REGRA: Qualquer pessoa que tenha uma conta (incluindo o Aluno) pode ler o mural
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/academia/{academiaId}")
    public ResponseEntity<List<Aviso>> verMural(@PathVariable Long academiaId) {
        return ResponseEntity.ok(avisoService.listarMuralDaAcademia(academiaId));
    }

    // REGRA: Só o Gerente pode apagar/inativar um aviso
    @PreAuthorize("hasAnyRole('GERENTE','ATENDENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerDoMural(@PathVariable Long id) {
        try {
            avisoService.inativarAviso(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}