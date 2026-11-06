package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.PerfilPrestador;

import java.math.BigDecimal;
import java.util.UUID;

public record PerfilPrestadorResponseDTO(
        UUID id,
        UUID usuarioId,
        String usuarioNome,
        String biografia,
        Boolean ativo,
        Boolean verificadoAntecedentes,
        Boolean verificadoTecnico,
        BigDecimal latitudeBase,
        BigDecimal longitudeBase,
        Integer raioAtendimentoKm
) {
    public static PerfilPrestadorResponseDTO de(PerfilPrestador p) {
        return new PerfilPrestadorResponseDTO(
                p.getId(),
                p.getUsuario().getId(),
                p.getUsuario().getNome(),
                p.getBiografia(),
                p.getAtivo(),
                p.getVerificadoAntecedentes(),
                p.getVerificadoTecnico(),
                p.getLatitudeBase(),
                p.getLongitudeBase(),
                p.getRaioAtendimentoKm());
    }
}
