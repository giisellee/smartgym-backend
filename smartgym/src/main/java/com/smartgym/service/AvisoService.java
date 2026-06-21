package com.smartgym.service;

import com.smartgym.model.Aviso;
import com.smartgym.repository.AvisoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvisoService {
    private final AvisoRepository avisoRepository;

    public AvisoService(AvisoRepository avisoRepository) {
        this.avisoRepository = avisoRepository;
    }

    public Aviso publicarAviso(Aviso aviso) {
        if (aviso.getTitulo() == null || aviso.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("O aviso precisa de um título.");
        }
        if (aviso.getAcademia() == null || aviso.getAcademia().getId() == null) {
            throw new IllegalArgumentException("O aviso precisa estar vinculado a um mural de uma academia.");
        }
        aviso.setAtivo(true);
        return avisoRepository.save(aviso);
    }

    public List<Aviso> listarMuralDaAcademia(Long academiaId) {
        return avisoRepository.findAllByAcademiaIdAndAtivoTrue(academiaId);
    }

    // Deleção Lógica: O aviso sai do mural, mas fica no histórico do banco
    public void inativarAviso(Long id) {
        Aviso aviso = avisoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Aviso não encontrado."));
        aviso.setAtivo(false);
        avisoRepository.save(aviso);
    }
}