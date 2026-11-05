package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.enums.MetodoPagamento;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PagamentoCreateDTO(
        @NotNull UUID orcamentoId,
        @NotNull MetodoPagamento metodo
) {
}
