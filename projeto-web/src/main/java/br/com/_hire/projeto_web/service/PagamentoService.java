package br.com._hire.projeto_web.service;

import br.com._hire.projeto_web.dto.PagamentoCreateDTO;
import br.com._hire.projeto_web.dto.PagamentoResponseDTO;
import br.com._hire.projeto_web.entity.Pagamento;
import br.com._hire.projeto_web.entity.SolicitacaoOrcamento;
import br.com._hire.projeto_web.enums.StatusOrcamento;
import br.com._hire.projeto_web.enums.StatusPagamento;
import br.com._hire.projeto_web.exception.ConflitoException;
import br.com._hire.projeto_web.exception.RecursoNaoEncontradoException;
import br.com._hire.projeto_web.repository.PagamentoRepository;
import br.com._hire.projeto_web.repository.SolicitacaoOrcamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private static final Set<StatusOrcamento> STATUS_QUE_PERMITEM_PAGAMENTO = EnumSet.of(
            StatusOrcamento.ACEITO, StatusOrcamento.AGENDADO,
            StatusOrcamento.EM_ANDAMENTO, StatusOrcamento.CONCLUIDO);

    private static final Set<StatusPagamento> STATUS_EM_ABERTO_OU_PAGO = EnumSet.of(
            StatusPagamento.PENDENTE, StatusPagamento.PAGO);

    private final PagamentoRepository pagamentoRepository;
    private final SolicitacaoOrcamentoRepository orcamentoRepository;

    @Transactional
    public PagamentoResponseDTO criar(PagamentoCreateDTO dto) {
        SolicitacaoOrcamento orcamento = orcamentoRepository.findById(dto.orcamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orçamento não encontrado: " + dto.orcamentoId()));

        if (!STATUS_QUE_PERMITEM_PAGAMENTO.contains(orcamento.getStatusAtual())) {
            throw new ConflitoException("Só é possível pagar orçamentos aceitos, agendados, em andamento ou concluídos");
        }
        BigDecimal valor = orcamento.getValorTotal();
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflitoException("O orçamento não possui valor a ser pago");
        }
        if (pagamentoRepository.existsByOrcamentoIdAndStatusIn(orcamento.getId(), STATUS_EM_ABERTO_OU_PAGO)) {
            throw new ConflitoException("Já existe um pagamento pendente ou pago para este orçamento");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setOrcamento(orcamento);
        pagamento.setMetodo(dto.metodo());
        pagamento.setValor(valor);
        return PagamentoResponseDTO.de(pagamentoRepository.save(pagamento));
    }

    @Transactional(readOnly = true)
    public PagamentoResponseDTO buscarPorId(UUID id) {
        return PagamentoResponseDTO.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarPorOrcamento(UUID orcamentoId) {
        if (!orcamentoRepository.existsById(orcamentoId)) {
            throw new RecursoNaoEncontradoException("Orçamento não encontrado: " + orcamentoId);
        }
        return pagamentoRepository.findByOrcamentoIdOrderByDataCriacaoDesc(orcamentoId).stream()
                .map(PagamentoResponseDTO::de).toList();
    }

    /** PENDENTE -> PAGO. */
    @Transactional
    public PagamentoResponseDTO confirmar(UUID id) {
        Pagamento pagamento = buscarEntidade(id);
        exigirStatus(pagamento, StatusPagamento.PENDENTE, "confirmar");
        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(LocalDateTime.now());
        return PagamentoResponseDTO.de(pagamentoRepository.save(pagamento));
    }

    /** PENDENTE -> FALHOU. */
    @Transactional
    public PagamentoResponseDTO falhar(UUID id) {
        Pagamento pagamento = buscarEntidade(id);
        exigirStatus(pagamento, StatusPagamento.PENDENTE, "marcar como falho");
        pagamento.setStatus(StatusPagamento.FALHOU);
        return PagamentoResponseDTO.de(pagamentoRepository.save(pagamento));
    }

    /** PAGO -> ESTORNADO. */
    @Transactional
    public PagamentoResponseDTO estornar(UUID id) {
        Pagamento pagamento = buscarEntidade(id);
        exigirStatus(pagamento, StatusPagamento.PAGO, "estornar");
        pagamento.setStatus(StatusPagamento.ESTORNADO);
        return PagamentoResponseDTO.de(pagamentoRepository.save(pagamento));
    }

    Pagamento buscarEntidade(UUID id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado: " + id));
    }

    private void exigirStatus(Pagamento pagamento, StatusPagamento esperado, String acao) {
        if (pagamento.getStatus() != esperado) {
            throw new ConflitoException("Não é possível " + acao + " um pagamento com status " + pagamento.getStatus());
        }
    }
}
