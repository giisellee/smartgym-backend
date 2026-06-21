package com.smartgym.service;

import com.smartgym.model.AvaliacaoFisica;
import com.smartgym.repository.AvaliacaoFisicaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvaliacaoFisicaService {
    private final AvaliacaoFisicaRepository avaliacaoRepository;

    public AvaliacaoFisicaService(AvaliacaoFisicaRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public AvaliacaoFisica registrarAvaliacao(AvaliacaoFisica avaliacao) {
        if (avaliacao.getAluno() == null || avaliacao.getAluno().getId() == null) {
            throw new IllegalArgumentException("A avaliação deve ser associada a um aluno.");
        }
        if (avaliacao.getPeso() <= 0 || avaliacao.getAltura() <= 0) {
            throw new IllegalArgumentException("Peso e altura devem ser maiores que zero.");
        }

        // REGRA DE NEGÓCIO: Calcula o IMC automaticamente antes de salvar no banco
        Double imcCalculado = avaliacao.getPeso() / (avaliacao.getAltura() * avaliacao.getAltura());
        avaliacao.setImc(imcCalculado);

        return avaliacaoRepository.save(avaliacao);
    }

    public List<AvaliacaoFisica> listarEvolucaoDoAluno(Long alunoId) {
        return avaliacaoRepository.findAllByAlunoId(alunoId);
    }
}