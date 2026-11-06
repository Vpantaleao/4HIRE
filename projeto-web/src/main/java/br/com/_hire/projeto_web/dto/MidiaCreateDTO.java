package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.enums.TipoMidia;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Informe apenas o dono que combina com o tipo:
 * PORTFOLIO e ANTECEDENTES -> perfilPrestadorId;
 * ANEXO_ORCAMENTO -> orcamentoId;
 * ANEXO_CHAT -> mensagemId.
 */
public record MidiaCreateDTO(
        @NotNull TipoMidia tipoMidia,
        @NotBlank @Size(max = 500) String urlArquivo,
        UUID perfilPrestadorId,
        UUID orcamentoId,
        UUID mensagemId
) {
    @AssertTrue(message = "Informe somente o dono compatível com o tipo: "
            + "PORTFOLIO/ANTECEDENTES -> perfilPrestadorId, ANEXO_ORCAMENTO -> orcamentoId, "
            + "ANEXO_CHAT -> mensagemId")
    public boolean isDonoConsistente() {
        if (tipoMidia == null) {
            return true; // @NotNull já reporta
        }
        boolean perfil = perfilPrestadorId != null;
        boolean orcamento = orcamentoId != null;
        boolean mensagem = mensagemId != null;
        return switch (tipoMidia) {
            case PORTFOLIO, ANTECEDENTES -> perfil && !orcamento && !mensagem;
            case ANEXO_ORCAMENTO -> orcamento && !perfil && !mensagem;
            case ANEXO_CHAT -> mensagem && !perfil && !orcamento;
        };
    }
}
