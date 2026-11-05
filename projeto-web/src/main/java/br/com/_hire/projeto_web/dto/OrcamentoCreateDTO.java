package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.enums.TurnoPreferencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrcamentoCreateDTO(
        @NotNull UUID clienteId,
        @NotNull UUID perfilPrestadorId,
        @NotNull Integer categoriaId,
        @NotNull UUID conversaId,
        @NotBlank String descricaoServico,
        @NotBlank String enderecoTexto,
        BigDecimal latitudeServico,
        BigDecimal longitudeServico,
        LocalDateTime dataAgendamento,
        TurnoPreferencia turnoPreferencia
) {
}
