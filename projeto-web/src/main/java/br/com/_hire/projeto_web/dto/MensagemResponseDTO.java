package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.Mensagem;

import java.time.LocalDateTime;
import java.util.UUID;

public record MensagemResponseDTO(
        UUID id,
        UUID conversaId,
        UUID remetenteId,
        String conteudoTexto,
        LocalDateTime dataEnvio,
        LocalDateTime lidaEm
) {
    public static MensagemResponseDTO de(Mensagem m) {
        return new MensagemResponseDTO(
                m.getId(),
                m.getConversa().getId(),
                m.getRemetente().getId(),
                m.getConteudoTexto(),
                m.getDataEnvio(),
                m.getLidaEm());
    }
}