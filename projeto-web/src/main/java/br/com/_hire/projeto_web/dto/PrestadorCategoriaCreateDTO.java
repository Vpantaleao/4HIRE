package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record PrestadorCategoriaCreateDTO(
        @NotNull UUID perfilPrestadorId,
        @NotNull Integer categoriaId,
        @NotNull @PositiveOrZero BigDecimal precoBaseCategoria
) {
}
