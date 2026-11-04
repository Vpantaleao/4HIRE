package br.com._hire.projeto_web.controller;

import br.com._hire.projeto_web.dto.AvaliacaoCreateDTO;
import br.com._hire.projeto_web.dto.AvaliacaoResponseDTO;
import br.com._hire.projeto_web.dto.MediaAvaliacaoDTO;
import br.com._hire.projeto_web.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoResponseDTO criar(@Valid @RequestBody AvaliacaoCreateDTO dto) {
        return service.criar(dto);
    }

    @GetMapping("/{id}")
    public AvaliacaoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/prestador/{perfilPrestadorId}")
    public List<AvaliacaoResponseDTO> porPrestador(@PathVariable UUID perfilPrestadorId) {
        return service.listarPorPrestador(perfilPrestadorId);
    }

    @GetMapping("/prestador/{perfilPrestadorId}/media")
    public MediaAvaliacaoDTO media(@PathVariable UUID perfilPrestadorId) {
        return service.media(perfilPrestadorId);
    }
}