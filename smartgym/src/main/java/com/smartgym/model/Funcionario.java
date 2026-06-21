package com.smartgym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "funcionarios")
// Prepara a herança para Atendente, Instrutor e Gerente
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class Funcionario extends Usuario {

    // Atributos do Diagrama
    private LocalDate dataNascimento;
    private String cargo;
    private Double salario;
    private LocalDate dataAdmissao;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // CORREÇÃO: Usar getCargo() em vez de this.cargo protege o código contra os Proxies do Hibernate
        if (this.getCargo() == null) {
            return List.of();
        }

        String cargoFormatado = "ROLE_" + this.getCargo().toUpperCase().trim().replace(" ", "_");
        return List.of(new SimpleGrantedAuthority(cargoFormatado));
    }

}