package com.smartgym.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class AvaliacaoFisica {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataAvaliacao;
    private Double peso;
    private Double altura;
    private Double imc;
    private Double percentualGordura;
    private Double massaMagra;
    private String observacao;

    @ManyToOne @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @ManyToOne @JoinColumn(name = "id_academia", nullable = false)
    private Academia academia;
}