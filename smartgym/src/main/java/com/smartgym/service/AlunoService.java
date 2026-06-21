package com.smartgym.service;

import com.smartgym.model.Aluno;
import com.smartgym.repository.AlunoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    // Injeção de dependências via Construtor (Melhor prática de OOP para manter o encapsulamento)
    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;

    public AlunoService(AlunoRepository alunoRepository, PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE (Ajustado para o fluxo de Primeiro Acesso)
    public Aluno salvar(Aluno aluno) {
        validarDadosBasicos(aluno);

        if (alunoRepository.existsByEmail(aluno.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado no sistema.");
        }

        // REGRA DE NEGÓCIO: Senha Padrão de Primeiro Acesso
        String senhaPadrao = "SmartGym@123";

        // Criptografamos a senha padrão antes de salvar
        aluno.setSenha(passwordEncoder.encode(senhaPadrao));

        return alunoRepository.save(aluno);
    }

    // READ (CORREÇÃO MULTITENANT): Substituído o findAll() genérico pelo filtro por ID da Academia
    public List<Aluno> listarPorAcademia(Long academiaId) {
        return alunoRepository.findAllByAcademiaId(academiaId);
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }

    // UPDATE
    public Aluno atualizar(Long id, Aluno dadosAtualizados) {
        return alunoRepository.findById(id).map(aluno -> {

            // Valida as novas informações vindas da requisição
            validarDadosBasicos(dadosAtualizados);

            // Regra de Negócio: Se alterou o e-mail, verifica se o novo já pertence a outra pessoa
            if (!aluno.getEmail().equals(dadosAtualizados.getEmail()) && alunoRepository.existsByEmail(dadosAtualizados.getEmail())) {
                throw new IllegalArgumentException("O novo e-mail informado já está em uso.");
            }

            // Atualiza os dados cadastrais herdados de Usuario
            aluno.setNome(dadosAtualizados.getNome());
            aluno.setEmail(dadosAtualizados.getEmail());
            aluno.setTelefone(dadosAtualizados.getTelefone());

            // Atualiza os dados médicos e físicos específicos de Aluno
            aluno.setDataNascimento(dadosAtualizados.getDataNascimento());
            aluno.setSexo(dadosAtualizados.getSexo());
            aluno.setObjetivo(dadosAtualizados.getObjetivo());
            aluno.setAltura(dadosAtualizados.getAltura());
            aluno.setPeso(dadosAtualizados.getPeso());

            // Segurança: Só altera a senha se o usuário digitou uma nova no formulário
            if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().trim().isEmpty()) {
                aluno.setSenha(passwordEncoder.encode(dadosAtualizados.getSenha()));
            }

            return alunoRepository.save(aluno);

        }).orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado com o ID: " + id));
    }

    // DELETE
    // Substitui a exclusão física pela INATIVAÇÃO LÓGICA
    public void inativar(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        aluno.setAtivo(false); // Remove o acesso, mas mantém o histórico
        alunoRepository.save(aluno);
    }

    public void reativar(Long id) {
        Aluno aluno = alunoRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        aluno.setAtivo(true);
        alunoRepository.save(aluno);
    }

    // Ação específica para o Aluno trocar a sua senha com segurança
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        // 1. Verifica se a senha antiga digitada bate com a que está no banco (a senha padrão)
        if (!passwordEncoder.matches(senhaAtual, aluno.getSenha())) {
            throw new IllegalArgumentException("A senha atual informada está incorreta.");
        }

        // 2. Valida se a nova senha não está vazia
        if (novaSenha == null || novaSenha.trim().length() < 6) {
            throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
        }

        // 3. Criptografa a nova senha e salva
        aluno.setSenha(passwordEncoder.encode(novaSenha));
        alunoRepository.save(aluno);
    }

    // --- Regras de Negócio Privadas (Princípio DRY) ---
    private void validarDadosBasicos(Aluno aluno) {
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }
        if (aluno.getEmail() == null || aluno.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }
        // A REGRA DE OURO DO MULTITENANT
        if (aluno.getAcademia() == null || aluno.getAcademia().getId() == null) {
            throw new IllegalArgumentException("Todo utilizador deve estar vinculado a uma Academia (idAcademia).");
        }
    }
}