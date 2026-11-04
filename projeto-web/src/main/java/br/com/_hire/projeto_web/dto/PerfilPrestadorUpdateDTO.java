package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PerfilPrestadorUpdateDTO(
        String biografia,
        @NotNull @Positive Integer raioAtendimentoKm,
        @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitudeBase,
        @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitudeBase
) {
    @AssertTrue(message = "latitudeBase e longitudeBase devem ser informadas juntas")
    public boolean isCoordenadasConsistentes() {
        return (latitudeBase == null) == (longitudeBase == null);
    }
}
