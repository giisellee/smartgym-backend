package com.smartgym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "alunos")
@Getter
@Setter
// A palavra 'extends' aplica o conceito de Herança. Aluno herda tudo de Usuario (nome, email, senha, academia, etc.)
public class Aluno extends Usuario {

    // Relacionamento com o Plano financeiro do aluno
    @ManyToOne
    private Plano plano;

    // Atributos específicos do Aluno exigidos pelo diagrama de classes
    private LocalDate dataNascimento;
    private String sexo;
    private String objetivo;
    private Double altura;
    private Double peso;
    private LocalDate dataCadastroPlano;

    // MÉTODO DERIVADO: Não vira uma coluna física no banco de dados.
    // É calculado dinamicamente quando o sistema solicita.
    public Double calcularFrequencia() {
        // Futuramente buscará dados do TreinoRepository
        // Fórmula: (treinos_concluidos / treinos_planejados) * 100
        return 0.0;
    }

}