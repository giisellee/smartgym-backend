package com.smartgym.repository;

import com.smartgym.model.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AvisoRepository extends JpaRepository<Aviso, Long> {
    // Busca avisos apenas daquela filial e que estejam ativos no mural
    List<Aviso> findAllByAcademiaIdAndAtivoTrue(Long academiaId);
}