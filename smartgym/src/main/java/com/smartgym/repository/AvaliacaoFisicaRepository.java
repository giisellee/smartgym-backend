package com.smartgym.repository;

import com.smartgym.model.AvaliacaoFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AvaliacaoFisicaRepository extends JpaRepository<AvaliacaoFisica, Long> {
    // Permite buscar o histórico de evolução do aluno
    List<AvaliacaoFisica> findAllByAlunoId(Long alunoId);
}