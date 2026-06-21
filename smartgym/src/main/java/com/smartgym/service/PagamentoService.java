package com.smartgym.service;

import com.smartgym.model.Pagamento;
import com.smartgym.model.StatusPagamento;
import com.smartgym.repository.PagamentoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class PagamentoService {
    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    // Cria a cobrança no sistema
    public Pagamento gerarCobranca(Pagamento pagamento) {
        if (pagamento.getValor() == null || pagamento.getValor() <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser superior a zero.");
        }
        if (pagamento.getAluno() == null || pagamento.getAcademia() == null) {
            throw new IllegalArgumentException("A cobrança exige um aluno e uma academia.");
        }

        pagamento.setStatus(StatusPagamento.PENDENTE); // Toda cobrança nasce pendente
        return pagamentoRepository.save(pagamento);
    }

    // Ação do atendente ao confirmar o recebimento
    public void confirmarPagamento(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cobrança não encontrada."));

        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(LocalDate.now()); // Regista o dia exato em que o dinheiro entrou
        pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> listarCaixaDaAcademia(Long academiaId) {
        return pagamentoRepository.findAllByAcademiaId(academiaId);
    }

    public List<Pagamento> listarFaturasDoAluno(Long alunoId) {
        return pagamentoRepository.findAllByAlunoId(alunoId);
    }
}