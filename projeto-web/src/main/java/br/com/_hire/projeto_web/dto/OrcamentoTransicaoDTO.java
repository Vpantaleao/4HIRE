package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrcamentoTransicaoDTO(
        @NotNull UUID usuarioId,
        String observacao
) {
}
