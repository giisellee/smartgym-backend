package com.smartgym.controller;

import com.smartgym.model.Academia;
import com.smartgym.service.AcademiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Sinaliza que esta classe vai se comunicar com a web através do formato JSON
@RequestMapping("/academias") // Define o ENDEREÇO BASE da API: http://localhost:8080/academias
public class AcademiaController {

    // Trazendo a camada Service para gerenciar as regras de negócio
    private final AcademiaService academiaService;

    // Injeção de dependência via Construtor: garante acoplamento seguro e facilita testes unitários
    public AcademiaController(AcademiaService academiaService) {
        this.academiaService = academiaService;
    }

    // --- ROTA POST: CADASTRAR UMA NOVA ACADEMIA ---
    // URL: http://localhost:8080/academias
    @PreAuthorize("hasRole('GERENTE')")
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Academia academia) {
        try {
            // Tenta salvar enviando o objeto para as validações do Service
            Academia novaAcademia = academiaService.salvarAcademia(academia);

            // Retorna o Status 201 Created padrão do protocolo REST para criações bem-sucedidas
            return ResponseEntity.status(HttpStatus.CREATED).body(novaAcademia);

        } catch (IllegalArgumentException e) {
            // Se o Service disparar um erro (ex: nome duplicado), devolvemos Status 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // --- ROTA GET: LISTAR TODAS AS UNIDADES ---
    // URL: http://localhost:8080/academias
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Academia>> listarTodas() {
        List<Academia> lista = academiaService.listarTodas();

        // Devolve o Status 200 OK acompanhado do array de academias convertido em JSON
        return ResponseEntity.ok(lista);
    }

    // --- ROTA PUT: ATUALIZAR UMA ACADEMIA POR ID ---
    // URL: http://localhost:8080/academias/{id}
    @PreAuthorize("hasRole('GERENTE')")
    @PutMapping("/{id}") // O {id} na URL é capturado dinamicamente
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Academia academia) {
        try {
            // Repassa o ID extraído da URL e o corpo JSON para o método de edição do Service
            Academia academiaAtualizada = academiaService.atualizarAcademia(id, academia);

            // Retorna Status 200 OK com o objeto já atualizado refletindo na tela
            return ResponseEntity.ok(academiaAtualizada);

        } catch (IllegalArgumentException e) {
            // Retorna o motivo exato pelo qual a validação barrou a edição (Status 400)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // --- ROTA DELETE: INATIVAR (DELEÇÃO LÓGICA) UMA ACADEMIA ---
    // URL: http://localhost:8080/academias/{id}
    @PreAuthorize("hasRole('GERENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            // Alinhamento estrito com o Service: desativa a filial mudando o status para false
            academiaService.inativarAcademia(id);

            // Retorna o Status 204 No Content: a operação funcionou e não há corpo extra a ser exibido
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            // Se tentar inativar uma academia com ID inexistente, cai aqui
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}