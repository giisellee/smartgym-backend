package com.smartgym.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exercicios")
@Getter
@Setter
public class Exercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome; // Ex: "Chest Press", "Peck Deck", "Leg Press"

    @Column(nullable = false)
    private String grupoMuscular; // Ex: "Peito", "Pernas"

    private Integer repeticoes; // Quantidade base sugerida
    private Double carga; // Carga base sugerida em kg
    private String observacao; // Dicas de execução física correta

    @ManyToOne
    @JoinColumn(name = "id_academia", nullable = false)
    private Academia academia;

    // NOVO: Deleção lógica para não quebrar o histórico de treinos dos alunos
    private Boolean ativo = true;
}