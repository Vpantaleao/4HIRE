package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.ChatConversa;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConversaResponseDTO(
        UUID id,
        UUID clienteId,
        UUID perfilPrestadorId,
        LocalDateTime dataCriacao
) {
    public static ConversaResponseDTO de(ChatConversa c) {
        return new ConversaResponseDTO(
                c.getId(),
                c.getCliente().getId(),
                c.getPerfilPrestador().getId(),
                c.getDataCriacao());
    }
}