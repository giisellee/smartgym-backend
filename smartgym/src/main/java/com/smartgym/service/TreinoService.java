package com.smartgym.service;

import com.smartgym.model.Treino;
import com.smartgym.model.Aluno;
import com.smartgym.repository.TreinoRepository;
import com.smartgym.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TreinoService {

    private final TreinoRepository treinoRepository;
    private final AlunoRepository alunoRepository; // Necessário para cruzar os dados

    public TreinoService(TreinoRepository treinoRepository, AlunoRepository alunoRepository) {
        this.treinoRepository = treinoRepository;
        this.alunoRepository = alunoRepository;
    }

    public Treino salvarTreino(Treino treino) {
        if (treino.getAcademia() == null || treino.getAcademia().getId() == null) {
            throw new IllegalArgumentException("O treino deve ser vinculado a uma Academia.");
        }
        if (treino.getAluno() == null || treino.getAluno().getId() == null) {
            throw new IllegalArgumentException("O treino precisa de um Aluno de destino.");
        }

        // Regra de Segurança: Verifica se o aluno realmente pertence àquela filial
        Aluno alunoBanco = alunoRepository.findById(treino.getAluno().getId())
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        if (!alunoBanco.getAcademia().getId().equals(treino.getAcademia().getId())) {
            throw new IllegalArgumentException("Operação bloqueada: Este aluno não pertence a esta filial.");
        }

        return treinoRepository.save(treino);
    }

    // Usado pelo aplicativo mobile do aluno para ver a sua própria lista
    public List<Treino> buscarTreinosDoAluno(Long alunoId, Long academiaId) {
        return treinoRepository.findByAlunoIdAndAcademiaId(alunoId, academiaId);
    }

    // Ação acionada quando o aluno termina a rotina na academia
    public void concluirTreino(Long id) {
        Treino treino = treinoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado."));

        treino.setConcluido(true);
        treinoRepository.save(treino);
    }
}