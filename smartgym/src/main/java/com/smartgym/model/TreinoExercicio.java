package com.smartgym.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TreinoExercicio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer ordem;
    private Integer descansoSegundos;
    private Boolean concluido = false;

    @ManyToOne @JoinColumn(name = "id_treino", nullable = false)
    private Treino treino;

    @ManyToOne @JoinColumn(name = "id_exercicio", nullable = false)
    private Exercicio exercicio;
}