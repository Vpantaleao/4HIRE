package br.com._hire.projeto_web.dto;

import java.util.UUID;

public record MediaAvaliacaoDTO(UUID perfilPrestadorId, double media, long total) {
}