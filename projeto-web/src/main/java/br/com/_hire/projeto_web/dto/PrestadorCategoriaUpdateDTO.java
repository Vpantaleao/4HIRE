package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PrestadorCategoriaUpdateDTO(
        @NotNull @PositiveOrZero BigDecimal precoBaseCategoria
) {
}
