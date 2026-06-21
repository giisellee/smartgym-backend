package com.smartgym.repository;

import com.smartgym.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    // Isolamento Multitenant: Lista apenas a equipe daquela academia informada
    List<Funcionario> findAllByAcademiaIdAndAtivoTrue(Long academiaId);

    boolean existsByEmail(String email);
}