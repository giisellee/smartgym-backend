package com.smartgym.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Pagamento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private Double valor;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    @ManyToOne @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @ManyToOne @JoinColumn(name = "id_academia", nullable = false)
    private Academia academia;
}