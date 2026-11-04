package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConversaCreateDTO(
        @NotNull UUID clienteId,
        @NotNull UUID perfilPrestadorId
) {
}