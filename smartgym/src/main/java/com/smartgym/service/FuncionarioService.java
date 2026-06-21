package com.smartgym.service;

import com.smartgym.model.Funcionario;
import com.smartgym.repository.FuncionarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Graças ao Polimorfismo, este método salva Gerente, Instrutor ou Atendente automaticamente!
    public Funcionario salvar(Funcionario funcionario) {
        validarDadosBasicos(funcionario);

        if (funcionarioRepository.existsByEmail(funcionario.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado para outro colaborador.");
        }

        // Criptografia obrigatória de segurança
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        return funcionarioRepository.save(funcionario);
    }

    // CORREÇÃO MULTITENANT: Filtra os funcionários pela unidade física de alocação
    public List<Funcionario> listarPorAcademia(Long academiaId) {
        return funcionarioRepository.findAllByAcademiaIdAndAtivoTrue(academiaId);
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    public Funcionario atualizar(Long id, Funcionario dadosAtualizados) {
        return funcionarioRepository.findById(id).map(func -> {

            validarDadosBasicos(dadosAtualizados);

            if (!func.getEmail().equals(dadosAtualizados.getEmail()) && funcionarioRepository.existsByEmail(dadosAtualizados.getEmail())) {
                throw new IllegalArgumentException("O e-mail informado já está em uso por outro funcionário.");
            }

            // Dados da classe mãe (Usuario)
            func.setNome(dadosAtualizados.getNome());
            func.setEmail(dadosAtualizados.getEmail());
            func.setTelefone(dadosAtualizados.getTelefone());

            // Dados específicos da classe filha (Funcionario)
            func.setDataNascimento(dadosAtualizados.getDataNascimento());
            func.setCargo(dadosAtualizados.getCargo());
            func.setSalario(dadosAtualizados.getSalario());
            func.setDataAdmissao(dadosAtualizados.getDataAdmissao());

            if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().trim().isEmpty()) {
                func.setSenha(passwordEncoder.encode(dadosAtualizados.getSenha()));
            }

            return funcionarioRepository.save(func);
        }).orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado com o ID: " + id));
    }

    // Substitui a exclusão física pela INATIVAÇÃO LÓGICA
    public void inativar(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));

        funcionario.setAtivo(false); // Remove o acesso, mas mantém o histórico financeiro
        funcionarioRepository.save(funcionario);
    }

    // Ação específica para recontratar/reativar o acesso de um funcionário
    public void reativar(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));

        funcionario.setAtivo(true); // Devolve o acesso ao sistema
        funcionarioRepository.save(funcionario);
    }

    private void validarDadosBasicos(Funcionario funcionario) {
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do funcionário é obrigatório.");
        }
        if (funcionario.getCargo() == null || funcionario.getCargo().trim().isEmpty()) {
            throw new IllegalArgumentException("O cargo do funcionário é obrigatório.");
        }
        if (funcionario.getSalario() == null || funcionario.getSalario() < 0) {
            throw new IllegalArgumentException("O salário deve ser um valor positivo.");
        }
        if (funcionario.getAcademia() == null || funcionario.getAcademia().getId() == null) {
            throw new IllegalArgumentException("Todo funcionário deve estar vinculado a uma Academia (idAcademia).");
        }
    }
}