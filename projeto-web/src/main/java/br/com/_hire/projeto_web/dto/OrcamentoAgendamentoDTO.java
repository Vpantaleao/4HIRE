package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.enums.TurnoPreferencia;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrcamentoAgendamentoDTO(
        @NotNull UUID usuarioId,
        @NotNull @Future LocalDateTime dataAgendamento,
        TurnoPreferencia turnoPreferencia
) {
}
