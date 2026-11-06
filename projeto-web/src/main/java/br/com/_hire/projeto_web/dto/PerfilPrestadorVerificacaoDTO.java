package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.NotNull;

public record PerfilPrestadorVerificacaoDTO(
        @NotNull Boolean verificadoAntecedentes,
        @NotNull Boolean verificadoTecnico
) {
}
