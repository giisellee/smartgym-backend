package com.smartgym.model;

import jakarta.persistence.*; // Importa todas as ferramentas de banco de dados do Spring
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 1. Transforma esta classe em uma tabela no banco de dados
@Getter // Cria todos os get() automaticamente
@Setter // Cria todos os set() automaticamente
@NoArgsConstructor // Cria o construtor vazio obrigatório do Spring
@AllArgsConstructor // Cria o construtor com todos os atributos
public class Academia {
    @Id //2. Avisa que este é o identificador único (a primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //3. O banco  vai gerar o ID automaticamente
    private Long id;

    @Column(nullable = false, unique = true) // 4. Regra: O nome não pode ser nulo e não podem existir duas academias com o mesmo nome
    private String nome;

    @Column(nullable = false)
    private String endereco;

    private String horarioFuncionamento;

    @ElementCollection // 5. Cria uma tabela auxiliar automática para guardar essa lista de textos
    private List<String> modalidades;

    @ElementCollection
    private List<String> maquinas;

}