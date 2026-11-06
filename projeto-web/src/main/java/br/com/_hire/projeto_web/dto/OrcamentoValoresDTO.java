package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record OrcamentoValoresDTO(
        @NotNull UUID usuarioId,
        @NotNull @PositiveOrZero BigDecimal valorVisitaTecnica,
        @NotNull @PositiveOrZero BigDecimal valorMaoDeObra,
        @NotNull @PositiveOrZero BigDecimal taxaPlataforma
) {
}
