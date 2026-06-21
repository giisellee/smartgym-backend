package com.smartgym.service;

import com.smartgym.model.Academia;
import com.smartgym.repository.AcademiaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Sinaliza que esta classe contém as regras de negócio (O cérebro do MVC)
public class AcademiaService {

    // Trazendo o Repository (a ponte com o banco) para dentro do Service
    private final AcademiaRepository academiaRepository;

    // Injeção de Dependência via Construtor (Melhor prática para testes e segurança)
    public AcademiaService(AcademiaRepository academiaRepository) {
        this.academiaRepository = academiaRepository;
    }

    // Método para salvar com Regra de Negócio e Tratamento de Exceções
    public Academia salvarAcademia(Academia academia) {

        // Validação estrutural dos dados que serão recebidos do Front-end
        validarDadosBasicos(academia);

        // REGRA DE NEGÓCIO ESPECÍFICA 01: Nome duplicado
        // Vai no banco e pergunta: "Já existe alguém com esse nome?"
        if (academiaRepository.existsByNome(academia.getNome())) {
            throw new IllegalArgumentException("Já existe uma unidade cadastrada com o nome: " + academia.getNome());
        }

        academia.setAtiva(true); // Força a nova academia a nascer ativa
        return academiaRepository.save(academia);
    }

    // Método simples para listar todas as unidades
    public List<Academia> listarTodas() {
        return academiaRepository.findAll(); // O Repository faz o SELECT * automático
    }

    // Método para ATUALIZAR (Editar) uma academia
    public Academia atualizarAcademia(Long id, Academia dadosAtualizados) {

        // 1. Busca a academia. Se não achar, "explode" um erro na cara do Front-end
        Academia academiaExistente = academiaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Academia não encontrada para edição."));

        // 2. Passa os dados que vieram do JSON JÁ VERIFICADOS pelo nosso cão de guarda
        validarDadosBasicos(dadosAtualizados);

        // REGRA DE NEGÓCIO ESPECÍFICA 02: Se mudou o nome, verifica se o novo nome já existe
        if (!academiaExistente.getNome().equals(dadosAtualizados.getNome()) &&
                academiaRepository.existsByNome(dadosAtualizados.getNome())) {
            throw new IllegalArgumentException("Já existe uma unidade com este novo nome.");
        }

        // 3. Atualiza os dados seguros (Removidas as antigas listas de modalidades e máquinas)
        academiaExistente.setNome(dadosAtualizados.getNome());
        academiaExistente.setEndereco(dadosAtualizados.getEndereco());
        academiaExistente.setHorarioFuncionamento(dadosAtualizados.getHorarioFuncionamento());
        academiaExistente.setTelefone(dadosAtualizados.getTelefone());
        academiaExistente.setEmail(dadosAtualizados.getEmail());

        return academiaRepository.save(academiaExistente); // O Hibernate percebe que já tem ID e faz um UPDATE
    }

    // Método para DESATIVAR (excluir de forma lógica) uma academia
    public void inativarAcademia(Long id) {
        // Verifica se a academia existe antes de tentar desativar
        Academia academia = academiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Academia não encontrada."));

        academia.setAtiva(false); // A mágica da deleção lógica
        academiaRepository.save(academia);
    }

    // Método auxiliar privado para garantir o princípio DRY (Don't Repeat Yourself)
    /* Esse método faz as verificações mais GERAIS dos dados que serão recebidos, evitando que alguma exceção passe despercebida */
    private void validarDadosBasicos(Academia academia) {
        if (academia.getNome() == null || academia.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da academia não pode estar vazio.");
        }
        if (academia.getEndereco() == null || academia.getEndereco().trim().isEmpty()) {
            throw new IllegalArgumentException("O endereço da academia é obrigatório.");
        }
        if (academia.getHorarioFuncionamento() == null || academia.getHorarioFuncionamento().trim().isEmpty()) {
            throw new IllegalArgumentException("O horário de funcionamento é obrigatório.");
        }
        // OBS: Validações de modalidades e máquinas foram removidas pois agora são gerenciadas de outra forma!
    }
}