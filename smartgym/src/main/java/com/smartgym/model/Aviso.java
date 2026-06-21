package com.smartgym.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Aviso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataPublicacao = LocalDateTime.now();
    private Boolean ativo = true;

    @ManyToOne @JoinColumn(name = "id_academia", nullable = false)
    private Academia academia;
}