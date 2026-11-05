package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.HistoricoStatus;
import br.com._hire.projeto_web.enums.StatusOrcamento;

import java.time.LocalDateTime;
import java.util.UUID;

public record HistoricoStatusResponseDTO(
        UUID id,
        UUID orcamentoId,
        StatusOrcamento statusAnterior,
        StatusOrcamento statusNovo,
        UUID alteradoPorId,
        String observacao,
        LocalDateTime dataAlteracao
) {
    public static HistoricoStatusResponseDTO de(HistoricoStatus h) {
        return new HistoricoStatusResponseDTO(
                h.getId(),
                h.getOrcamento().getId(),
                h.getStatusAnterior(),
                h.getStatusNovo(),
                h.getAlteradoPor().getId(),
                h.getObservacao(),
                h.getDataAlteracao());
    }
}
