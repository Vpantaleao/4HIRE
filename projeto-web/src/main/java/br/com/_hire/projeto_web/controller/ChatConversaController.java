package br.com._hire.projeto_web.controller;

import br.com._hire.projeto_web.dto.ConversaCreateDTO;
import br.com._hire.projeto_web.dto.ConversaResponseDTO;
import br.com._hire.projeto_web.service.ChatConversaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/conversas")
@RequiredArgsConstructor
public class ChatConversaController {

    private final ChatConversaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConversaResponseDTO criar(@Valid @RequestBody ConversaCreateDTO dto) {
        return service.criar(dto);
    }

    @GetMapping("/{id}")
    public ConversaResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<ConversaResponseDTO> porCliente(@PathVariable UUID clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @GetMapping("/prestador/{perfilPrestadorId}")
    public List<ConversaResponseDTO> porPrestador(@PathVariable UUID perfilPrestadorId) {
        return service.listarPorPrestador(perfilPrestadorId);
    }
}