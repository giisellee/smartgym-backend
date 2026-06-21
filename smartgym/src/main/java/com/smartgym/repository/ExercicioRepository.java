package com.smartgym.repository;

import com.smartgym.model.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {
    // Filtro Multitenant: O instrutor só vê os exercícios cadastrados na sua filial
    List<Exercicio> findAllByAcademiaIdAndAtivoTrue(Long academiaId);
}