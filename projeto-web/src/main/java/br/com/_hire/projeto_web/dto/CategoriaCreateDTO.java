package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaCreateDTO(
        @NotBlank @Size(max = 255) String nome,
        String descricao,
        Integer categoriaPaiId
) {
}
