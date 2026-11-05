package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MensagemCreateDTO(
        @NotNull UUID remetenteId,
        @NotBlank String conteudoTexto
) {
}