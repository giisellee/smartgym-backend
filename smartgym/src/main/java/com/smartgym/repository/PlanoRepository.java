package com.smartgym.repository;

import com.smartgym.model.Plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Interface que gera os comandos SQL para a tabela de Planos
public interface PlanoRepository extends JpaRepository<Plano, Long> {

    // Filtro de Segurança Multitenant: Retorna apenas os planos de uma filial específica!
    // O JPA transforma isto em: SELECT * FROM planos WHERE id_academia = ?
    List<Plano> findAllByAcademiaId(Long academiaId);

    // Validação extra: Impede que a mesma filial crie dois planos com o mesmo nome
    boolean existsByNomeAndAcademiaId(String nome, Long academiaId);
}