package com.smartgym.service;

import com.smartgym.model.Academia;
import com.smartgym.repository.AcademiaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //Sinaliza que esta classe contém as regras de negócio
public class AcademiaService {

    // Trazendo o Repository para dentro do Service
    private final AcademiaRepository academiaRepository;

    // Injeção de Dependência via Construtor
    public AcademiaService(AcademiaRepository academiaRepository) {
        this.academiaRepository = academiaRepository;
    }

    // Método para salvar com Regra de Negócio e Tratamento de Exceções
    public Academia salvarAcademia(Academia academia) {

        // Validação dos dados que serão recebidos
        validarDadosBasicos(academia);

        // REGRA DE NEGÓCIO ESPECÍFICA 01: Nome duplicado
        if (academiaRepository.existsByNome(academia.getNome())) {
            throw new IllegalArgumentException("Já existe uma unidade cadastrada com o nome: " + academia.getNome());
        }

        return academiaRepository.save(academia);
    }
    // 5. Método simples para listar todas as unidades
    public List<Academia> listarTodas() {
        return academiaRepository.findAll(); // O Repository faz o SELECT * automático
    }

    // Método para ATUALIZAR (Editar) uma academia
    public Academia atualizarAcademia(Long id, Academia dadosAtualizados) {

        Academia academiaExistente = academiaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Academia não encontrada para edição."));

        // Passa os dados que vieram do JSON JÁ VERIFICADOS
        validarDadosBasicos(dadosAtualizados);

        // REGRA DE NEGÓCIO ESPECÍFICA 01: Se mudou o nome, verifica se o novo nome já existe
        if (!academiaExistente.getNome().equals(dadosAtualizados.getNome()) &&
                academiaRepository.existsByNome(dadosAtualizados.getNome())) {
            throw new IllegalArgumentException("Já existe uma unidade com este novo nome.");
        }

        // 3. Atualiza os dados seguros
        academiaExistente.setNome(dadosAtualizados.getNome());
        academiaExistente.setEndereco(dadosAtualizados.getEndereco());
        academiaExistente.setHorarioFuncionamento(dadosAtualizados.getHorarioFuncionamento());
        academiaExistente.setModalidades(dadosAtualizados.getModalidades());
        academiaExistente.setMaquinas(dadosAtualizados.getMaquinas());

        return academiaRepository.save(academiaExistente);
    }

    // Método para EXCLUIR (Deletar) uma academia
    public void excluirAcademia(Long id) {
        // Verifica se a academia existe antes de tentar deletar
        if (!academiaRepository.existsById(id)) {
            throw new IllegalArgumentException("Academia não encontrada para exclusão.");
        }

        academiaRepository.deleteById(id);
    }

    // Método auxiliar privado para garantir o princípio DRY
    /*Esse método faz as verificações mais GERAIS dos dados que serão recebidos, evitando que alguma exceção passe despercebido*/
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
        // Validação para LISTAS: Verifica se é nula ou se está vazia (size == 0)
        if (academia.getModalidades() == null || academia.getModalidades().isEmpty()) {
            throw new IllegalArgumentException("A academia precisa ter pelo menos uma modalidade cadastrada.");
        }
        if (academia.getMaquinas() == null || academia.getMaquinas().isEmpty()) {
            throw new IllegalArgumentException("A academia precisa ter equipamentos cadastrados para funcionar.");
        }
    }
}