package com.smartgym.controller;

import com.smartgym.model.Academia;
import com.smartgym.service.AcademiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Sinaliza que esta classe vai se comunicar com a web (JSON)
@RequestMapping("/academias") // Define o ENDEREÇO BASE: http://localhost:8080/academias
public class AcademiaController {
    //Trazendo o Service para dentro do Controller
    private final AcademiaService academiaService;

    // Injeção de dependência: O Controller recebe o Service pronto para uso
    public AcademiaController(AcademiaService academiaService) {
        this.academiaService = academiaService;
    }

    // Porta de entrada para CADASTRAR uma academia (MÉTODO POST)
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Academia academia) {
        try {
            // Tenta salvar usando as regras do Service
            Academia novaAcademia = academiaService.salvarAcademia(academia);

            // Se deu certo, devolve o Status 201 (Created) e a academia salva com o ID gerado
            return ResponseEntity.status(HttpStatus.CREATED).body(novaAcademia);

        } catch (IllegalArgumentException e) {
            // Se o Service barrar a operação e lançar a exceção, o código chega aqui
            // Um Status 400 (Bad Request) e a mensagem de erro são devolvidas.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Porta de entrada para LISTAR todas as academias (Método GET)
    @GetMapping
    public ResponseEntity<List<Academia>> listarTodas() {
        List<Academia> lista = academiaService.listarTodas();

        // Devolve o Status 200 (OK) junto com a lista no formato JSON
        return ResponseEntity.ok(lista);
    }

    // Porta de entrada para ATUALIZAR (MÉTODO PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Academia academia) {
        try {
            // Repassa o ID da URL e o JSON do Body para o Service
            Academia academiaAtualizada = academiaService.atualizarAcademia(id, academia);

            // Devolve Status 200 (ok) e a academia atualizada
            return ResponseEntity.ok(academiaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            academiaService.excluirAcademia(id);

            // Status 204: Deletado com sucesso
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}