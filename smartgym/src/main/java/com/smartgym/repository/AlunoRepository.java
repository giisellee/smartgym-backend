package com.smartgym.repository;

import com.smartgym.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    // Filtro de isolamento Multitenant: Garante que só listamos os alunos da filial solicitada
    List<Aluno> findAllByAcademiaId(Long academiaId);

    // Validação de negócio: Permite checar se um e-mail já está cadastrado antes de salvar um novo aluno
    boolean existsByEmail(String email);
}