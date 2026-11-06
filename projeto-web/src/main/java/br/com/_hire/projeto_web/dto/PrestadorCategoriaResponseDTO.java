package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.PrestadorCategoria;

import java.math.BigDecimal;
import java.util.UUID;

public record PrestadorCategoriaResponseDTO(
        UUID perfilPrestadorId,
        Integer categoriaId,
        String categoriaNome,
        BigDecimal precoBaseCategoria
) {
    public static PrestadorCategoriaResponseDTO de(PrestadorCategoria pc) {
        return new PrestadorCategoriaResponseDTO(
                pc.getPerfilPrestador().getId(),
                pc.getCategoria().getId(),
                pc.getCategoria().getNome(),
                pc.getPrecoBaseCategoria());
    }
}
