package com.smartgym.repository;

import com.smartgym.model.Treino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TreinoRepository extends JpaRepository<Treino, Long> {
    // Traz o histórico de treinos de um aluno, garantindo que é da filial correta
    List<Treino> findByAlunoIdAndAcademiaId(Long alunoId, Long academiaId);
}