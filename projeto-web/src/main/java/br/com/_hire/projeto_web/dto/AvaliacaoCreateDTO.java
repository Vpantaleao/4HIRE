package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AvaliacaoCreateDTO(
        @NotNull UUID orcamentoId,
        @NotNull UUID avaliadorId,
        @NotNull @Min(1) @Max(5) Integer nota,
        String comentario
) {
}