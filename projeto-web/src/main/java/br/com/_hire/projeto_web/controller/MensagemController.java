package br.com._hire.projeto_web.controller;

import br.com._hire.projeto_web.dto.MensagemCreateDTO;
import br.com._hire.projeto_web.dto.MensagemResponseDTO;
import br.com._hire.projeto_web.service.MensagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/conversas/{conversaId}/mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MensagemResponseDTO enviar(@PathVariable UUID conversaId,
                                      @Valid @RequestBody MensagemCreateDTO dto) {
        return service.enviar(conversaId, dto);
    }

    @GetMapping
    public List<MensagemResponseDTO> listar(@PathVariable UUID conversaId) {
        return service.listar(conversaId);
    }

    @PatchMapping("/lidas")
    public Map<String, Integer> marcarComoLidas(@PathVariable UUID conversaId,
                                                @RequestParam UUID leitorId) {
        return Map.of("marcadas", service.marcarComoLidas(conversaId, leitorId));
    }

    @GetMapping("/nao-lidas")
    public Map<String, Long> naoLidas(@PathVariable UUID conversaId,
                                      @RequestParam UUID usuarioId) {
        return Map.of("naoLidas", service.contarNaoLidas(conversaId, usuarioId));
    }
}