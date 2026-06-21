package com.smartgym.repository;

import com.smartgym.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Interface que gera os comandos SQL (SELECT, INSERT, UPDATE, DELETE) automaticamente
public interface MaquinaRepository extends JpaRepository<Maquina, Long> {

    // Filtro de Segurança Multitenant: Retorna apenas os equipamentos de uma filial específica!
    // Tradução para SQL: SELECT * FROM maquinas WHERE id_academia = ?
    List<Maquina> findAllByAcademiaId(Long academiaId);

    // Validação extra (Opcional, mas recomendada):
    // Verifica se já existe uma máquina com esse exato nome e modelo naquela academia específica
    boolean existsByNomeAndModeloAndAcademiaId(String nome, String modelo, Long academiaId);
}