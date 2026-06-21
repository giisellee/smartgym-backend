package com.smartgym.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // 1. Indica ao Spring que esta classe vai virar uma tabela no PostgreSQL
@Table(name = "planos") // 2. Nomeia a tabela no plural por convenção
@Getter // 3. O Lombok gera os métodos get() de forma invisível
@Setter // 4. O Lombok gera os métodos set() de forma invisível
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária auto-incremental

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private Integer duracaoMeses; // Ex: 1 (Mensal), 12 (Anual)

    @Column(nullable = false)
    private Double valorMensal; // Preço do plano

    // Deleção Lógica: Planos antigos não são apagados (para não quebrar o histórico financeiro), são apenas inativados
    private Boolean ativo = true;

    // A MÁGICA DO MULTITENANT: Liga este plano a uma filial física específica
    @ManyToOne
    @JoinColumn(name = "id_academia", nullable = false)
    private Academia academia;
}