package com.smartgym.model;

import jakarta.persistence.*; // Importa todas as ferramentas de banco de dados do Spring
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 1. Transforma esta classe em uma tabela no banco de dados chamada "Academia"
@Getter // Cria todos os métodos get() automaticamente nos bastidores
@Setter // Cria todos os métodos set() automaticamente nos bastidores
@NoArgsConstructor // Cria o construtor vazio obrigatório do Spring (Hibernate precisa disso)
@AllArgsConstructor // Cria um construtor com todos os atributos
public class Academia {

    @Id // 2. Avisa que este é o identificador único (a primary key - PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 3. O banco de dados vai gerar o ID automaticamente (Auto-Incremento)
    private Long id;

    @Column(nullable = false, unique = true) // 4. Regra de integridade: O nome não pode ser nulo e não podem existir duas academias com o mesmo nome
    private String nome;

    @Column(nullable = false) // 5. Endereço é obrigatório para uma filial física
    private String endereco;

    // Atributos de contato e funcionamento da filial
    private String horarioFuncionamento;
    private String telefone;
    private String email;

    // 6. Deleção Lógica: Em vez de apagar do banco, nós apenas mudamos este status para false
    private Boolean ativa = true; // Define como ativa por padrão ao criar
}