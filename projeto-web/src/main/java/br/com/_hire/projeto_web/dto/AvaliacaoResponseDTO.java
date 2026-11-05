package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.Avaliacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvaliacaoResponseDTO(
        UUID id,
        UUID orcamentoId,
        UUID avaliadorId,
        UUID perfilPrestadorId,
        Integer nota,
        String comentario,
        LocalDateTime dataAvaliacao
) {
    public static AvaliacaoResponseDTO de(Avaliacao a) {
        return new AvaliacaoResponseDTO(
                a.getId(),
                a.getOrcamento().getId(),
                a.getAvaliador().getId(),
                a.getPerfilPrestador().getId(),
                a.getNota(),
                a.getComentario(),
                a.getDataAvaliacao());
    }
}