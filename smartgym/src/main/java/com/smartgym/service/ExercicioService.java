package com.smartgym.service;

import com.smartgym.model.Exercicio;
import com.smartgym.repository.ExercicioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExercicioService {

    private final ExercicioRepository exercicioRepository;

    public ExercicioService(ExercicioRepository exercicioRepository) {
        this.exercicioRepository = exercicioRepository;
    }

    public Exercicio salvar(Exercicio exercicio) {
        if (exercicio.getNome() == null || exercicio.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do exercício é obrigatório.");
        }
        if (exercicio.getGrupoMuscular() == null || exercicio.getGrupoMuscular().trim().isEmpty()) {
            throw new IllegalArgumentException("O grupo muscular é obrigatório.");
        }
        if (exercicio.getAcademia() == null || exercicio.getAcademia().getId() == null) {
            throw new IllegalArgumentException("O exercício deve pertencer a uma academia.");
        }
        return exercicioRepository.save(exercicio);
    }

    // ATUALIZADO: Usando o novo método do repository
    public List<Exercicio> listarPorAcademia(Long academiaId) {
        return exercicioRepository.findAllByAcademiaIdAndAtivoTrue(academiaId);
    }

    // NOVO: Método de Atualização (Editar)
    public Exercicio atualizar(Long id, Exercicio dadosAtualizados) {
        Exercicio exercicioExistente = exercicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercício não encontrado."));

        if (dadosAtualizados.getNome() == null || dadosAtualizados.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do exercício não pode estar vazio.");
        }

        // Atualiza os dados descritivos e técnicos
        exercicioExistente.setNome(dadosAtualizados.getNome());
        exercicioExistente.setGrupoMuscular(dadosAtualizados.getGrupoMuscular());
        exercicioExistente.setRepeticoes(dadosAtualizados.getRepeticoes());
        exercicioExistente.setCarga(dadosAtualizados.getCarga());
        exercicioExistente.setObservacao(dadosAtualizados.getObservacao());

        return exercicioRepository.save(exercicioExistente);
    }

    // NOVO: Método de Deleção Lógica
    public void inativar(Long id) {
        Exercicio exercicio = exercicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercício não encontrado."));

        exercicio.setAtivo(false); // O exercício some da lista de novos treinos, mas os treinos antigos não quebram
        exercicioRepository.save(exercicio);
    }
}