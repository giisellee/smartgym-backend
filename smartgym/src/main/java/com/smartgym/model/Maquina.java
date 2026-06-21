package com.smartgym.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity // 1. Indica ao Spring que esta classe representa uma tabela na base de dados
@Table(name = "maquinas") // 2. Nome da tabela no plural (boas práticas)
@Getter // 3. O Lombok gera os getters (ex: getNome()) invisivelmente
@Setter // 4. O Lombok gera os setters invisivelmente
public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único do equipamento

    @Column(nullable = false)
    private String nome; // Ex: "Leg Press 45", "Peck Deck"

    private String tipo; // Ex: "Cardio", "Musculação"
    private String marca; // Ex: "Matrix", "Life Fitness"
    private String modelo;

    // 5. Estado operacional da máquina (se está avariada ou não)
    private Boolean funcionando = true;

    private LocalDate dataAquisicao; // Quando foi comprada

    // A MÁGICA DO MULTITENANT: Liga este equipamento a uma filial específica
    // Impede que as máquinas da filial A apareçam na lista da filial B
    @ManyToOne
    @JoinColumn(name = "id_academia", nullable = false)
    private Academia academia;
}