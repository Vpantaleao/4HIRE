package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.Midia;
import br.com._hire.projeto_web.enums.TipoMidia;

import java.time.LocalDateTime;
import java.util.UUID;

public record MidiaResponseDTO(
        UUID id,
        TipoMidia tipoMidia,
        String urlArquivo,
        UUID perfilPrestadorId,
        UUID orcamentoId,
        UUID mensagemId,
        LocalDateTime criadoEm
) {
    public static MidiaResponseDTO de(Midia m) {
        return new MidiaResponseDTO(
                m.getId(),
                m.getTipoMidia(),
                m.getUrlArquivo(),
                m.getPerfilPrestador() == null ? null : m.getPerfilPrestador().getId(),
                m.getOrcamento() == null ? null : m.getOrcamento().getId(),
                m.getMensagem() == null ? null : m.getMensagem().getId(),
                m.getCriadoEm());
    }
}
