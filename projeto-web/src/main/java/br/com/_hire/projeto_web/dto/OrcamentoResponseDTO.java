package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.SolicitacaoOrcamento;
import br.com._hire.projeto_web.enums.StatusOrcamento;
import br.com._hire.projeto_web.enums.TurnoPreferencia;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrcamentoResponseDTO(
        UUID id,
        UUID clienteId,
        UUID perfilPrestadorId,
        Integer categoriaId,
        UUID conversaId,
        StatusOrcamento statusAtual,
        String descricaoServico,
        String enderecoTexto,
        BigDecimal latitudeServico,
        BigDecimal longitudeServico,
        LocalDateTime dataAgendamento,
        TurnoPreferencia turnoPreferencia,
        BigDecimal valorVisitaTecnica,
        BigDecimal valorMaoDeObra,
        BigDecimal taxaPlataforma,
        BigDecimal valorTotal,
        LocalDateTime dataSolicitacao
) {
    public static OrcamentoResponseDTO de(SolicitacaoOrcamento o) {
        return new OrcamentoResponseDTO(
                o.getId(),
                o.getCliente().getId(),
                o.getPerfilPrestador().getId(),
                o.getCategoria().getId(),
                o.getConversa().getId(),
                o.getStatusAtual(),
                o.getDescricaoServico(),
                o.getEnderecoTexto(),
                o.getLatitudeServico(),
                o.getLongitudeServico(),
                o.getDataAgendamento(),
                o.getTurnoPreferencia(),
                o.getValorVisitaTecnica(),
                o.getValorMaoDeObra(),
                o.getTaxaPlataforma(),
                o.getValorTotal(),
                o.getDataSolicitacao());
    }
}
