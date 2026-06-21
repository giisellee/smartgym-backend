package com.smartgym.repository;

import com.smartgym.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    // Permite que o gerente veja todo o caixa da sua filial específica
    List<Pagamento> findAllByAcademiaId(Long academiaId);

    // Permite que o aluno veja os seus próprios boletos/faturas
    List<Pagamento> findAllByAlunoId(Long alunoId);
}