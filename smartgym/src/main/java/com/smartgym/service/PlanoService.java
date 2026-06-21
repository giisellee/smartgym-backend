package com.smartgym.service;

import com.smartgym.model.Plano;
import com.smartgym.repository.PlanoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Sinaliza que esta classe contém as regras de negócio rigorosas do sistema
public class PlanoService {

    private final PlanoRepository planoRepository;

    // Injeção de dependência via construtor
    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    // --- CREATE ---
    public Plano salvarPlano(Plano plano) {

        // 1. Passa os dados pelo nosso "cão de guarda" estrutural
        validarDadosBasicos(plano);

        // 2. Regra de Negócio: A mesma filial não pode ter dois planos com nomes idênticos
        if (planoRepository.existsByNomeAndAcademiaId(plano.getNome(), plano.getAcademia().getId())) {
            throw new IllegalArgumentException("Esta academia já possui um plano com este nome.");
        }

        plano.setAtivo(true); // Todo plano novo nasce ativo
        return planoRepository.save(plano);
    }

    // --- READ (Com proteção Multitenant) ---
    // Repare que já não usamos o findAll() genérico. Obrigamos a informar a qual academia o gerente pertence.
    public List<Plano> listarPorAcademia(Long academiaId) {
        return planoRepository.findAllByAcademiaId(academiaId);
    }

    // --- UPDATE ---
    public Plano atualizarPlano(Long id, Plano dadosAtualizados) {
        Plano planoExistente = planoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado."));

        validarDadosBasicos(dadosAtualizados);

        // Se o gerente mudou o nome do plano, verifica se o novo nome já existe naquela filial
        if (!planoExistente.getNome().equals(dadosAtualizados.getNome()) &&
                planoRepository.existsByNomeAndAcademiaId(dadosAtualizados.getNome(), planoExistente.getAcademia().getId())) {
            throw new IllegalArgumentException("Já existe outro plano com este nome nesta unidade.");
        }

        // Atualiza apenas os dados financeiros e descritivos (Nunca mudamos a academia de um plano)
        planoExistente.setNome(dadosAtualizados.getNome());
        planoExistente.setDescricao(dadosAtualizados.getDescricao());
        planoExistente.setDuracaoMeses(dadosAtualizados.getDuracaoMeses());
        planoExistente.setValorMensal(dadosAtualizados.getValorMensal());

        return planoRepository.save(planoExistente);
    }

    // --- DELETE (Deleção Lógica) ---
    public void inativarPlano(Long id) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado."));

        // Impede que planos antigos sejam comprados, mas mantém-nos no banco para os alunos que já os têm
        plano.setAtivo(false);
        planoRepository.save(plano);
    }

    // --- VALIDAÇÕES PRIVADAS (Princípio DRY) ---
    private void validarDadosBasicos(Plano plano) {
        if (plano.getNome() == null || plano.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do plano é obrigatório.");
        }
        if (plano.getDuracaoMeses() == null || plano.getDuracaoMeses() <= 0) {
            throw new IllegalArgumentException("A duração do plano deve ser de pelo menos 1 mês.");
        }
        if (plano.getValorMensal() == null || plano.getValorMensal() < 0) {
            throw new IllegalArgumentException("O valor mensal não pode ser negativo.");
        }
        // REGRA DE OURO MULTITENANT
        if (plano.getAcademia() == null || plano.getAcademia().getId() == null) {
            throw new IllegalArgumentException("O plano tem de ser vinculado a uma Academia física (idAcademia).");
        }
    }
}