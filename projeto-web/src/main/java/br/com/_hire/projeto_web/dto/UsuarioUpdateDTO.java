package br.com._hire.projeto_web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String telefone
) {}
