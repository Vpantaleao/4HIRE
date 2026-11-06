package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.Categoria;

public record CategoriaResponseDTO(
        Integer id,
        String nome,
        String descricao,
        Integer categoriaPaiId
) {
    public static CategoriaResponseDTO de(Categoria c) {
        return new CategoriaResponseDTO(
                c.getId(),
                c.getNome(),
                c.getDescricao(),
                c.getCategoriaPai() == null ? null : c.getCategoriaPai().getId());
    }
}
