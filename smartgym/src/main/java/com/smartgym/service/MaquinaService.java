package com.smartgym.service;

import com.smartgym.model.Maquina;
import com.smartgym.repository.MaquinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Sinaliza que esta classe contém as regras de negócio rigorosas do sistema
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;

    // Injeção de dependência via construtor (Arquitetura segura)
    public MaquinaService(MaquinaRepository maquinaRepository) {
        this.maquinaRepository = maquinaRepository;
    }

    // --- CREATE ---
    public Maquina salvarMaquina(Maquina maquina) {
        // 1. Passa os dados pelo nosso "cão de guarda" estrutural
        validarDadosBasicos(maquina);

        // Toda máquina recém-registada começa a funcionar por padrão
        maquina.setFuncionando(true);
        return maquinaRepository.save(maquina);
    }

    // --- READ (Com proteção Multitenant) ---
    // O gerente só pode ver as máquinas da sua própria academia
    public List<Maquina> listarPorAcademia(Long academiaId) {
        return maquinaRepository.findAllByAcademiaId(academiaId);
    }

    // --- UPDATE (Editar informações descritivas) ---
    public Maquina atualizarMaquina(Long id, Maquina dadosAtualizados) {
        Maquina maquinaExistente = maquinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Máquina não encontrada."));

        validarDadosBasicos(dadosAtualizados);

        // Atualiza apenas os dados técnicos
        maquinaExistente.setNome(dadosAtualizados.getNome());
        maquinaExistente.setTipo(dadosAtualizados.getTipo());
        maquinaExistente.setMarca(dadosAtualizados.getMarca());
        maquinaExistente.setModelo(dadosAtualizados.getModelo());
        maquinaExistente.setDataAquisicao(dadosAtualizados.getDataAquisicao());

        return maquinaRepository.save(maquinaExistente);
    }

    // --- AÇÕES FÍSICAS (Conforme o Diagrama de Classes) ---

    // Coloca a máquina em estado de avaria/manutenção
    public void marcarManutencao(Long id) {
        Maquina maquina = maquinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Máquina não encontrada."));

        maquina.setFuncionando(false);
        maquinaRepository.save(maquina);
    }

    // Retorna a máquina ao estado operacional
    public void ativarMaquina(Long id) {
        Maquina maquina = maquinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Máquina não encontrada."));

        maquina.setFuncionando(true);
        maquinaRepository.save(maquina);
    }

    // --- VALIDAÇÕES PRIVADAS (Princípio DRY) ---
    /* Este método faz as verificações gerais */
    private void validarDadosBasicos(Maquina maquina) {
        if (maquina.getNome() == null || maquina.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da máquina é obrigatório.");
        }

        if (maquina.getAcademia() == null || maquina.getAcademia().getId() == null) {
            throw new IllegalArgumentException("O equipamento tem de ser vinculado a uma Academia física (idAcademia).");
        }
    }
}