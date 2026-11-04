package br.com._hire.projeto_web.dto;

import br.com._hire.projeto_web.entity.Usuario;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        String telefone,
        LocalDateTime criadoEm
) {
    public static UsuarioResponseDTO de(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(), u.getNome(), u.getEmail(), u.getTelefone(), u.getCriadoEm());
    }
}
