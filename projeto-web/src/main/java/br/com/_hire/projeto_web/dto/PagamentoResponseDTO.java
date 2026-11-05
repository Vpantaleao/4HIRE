package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.Pagamento;
import br.com._hire.projeto_web.enums.MetodoPagamento;
import br.com._hire.projeto_web.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagamentoResponseDTO(
        UUID id,
        UUID orcamentoId,
        MetodoPagamento metodo,
        StatusPagamento status,
        BigDecimal valor,
        LocalDateTime dataCriacao,
        LocalDateTime dataPagamento
) {
    public static PagamentoResponseDTO de(Pagamento p) {
        return new PagamentoResponseDTO(
                p.getId(),
                p.getOrcamento().getId(),
                p.getMetodo(),
                p.getStatus(),
                p.getValor(),
                p.getDataCriacao(),
                p.getDataPagamento());
    }
}
