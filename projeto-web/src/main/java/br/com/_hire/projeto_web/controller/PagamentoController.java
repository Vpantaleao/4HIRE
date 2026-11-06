package br.com._hire.projeto_web.controller;

import br.com._hire.projeto_web.dto.PagamentoCreateDTO;
import br.com._hire.projeto_web.dto.PagamentoResponseDTO;
import br.com._hire.projeto_web.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagamentoResponseDTO criar(@Valid @RequestBody PagamentoCreateDTO dto) {
        return service.criar(dto);
    }

    @GetMapping("/{id}")
    public PagamentoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/orcamento/{orcamentoId}")
    public List<PagamentoResponseDTO> porOrcamento(@PathVariable UUID orcamentoId) {
        return service.listarPorOrcamento(orcamentoId);
    }

    @PatchMapping("/{id}/confirmar")
    public PagamentoResponseDTO confirmar(@PathVariable UUID id) {
        return service.confirmar(id);
    }

    @PatchMapping("/{id}/falhar")
    public PagamentoResponseDTO falhar(@PathVariable UUID id) {
        return service.falhar(id);
    }

    @PatchMapping("/{id}/estornar")
    public PagamentoResponseDTO estornar(@PathVariable UUID id) {
        return service.estornar(id);
    }
}
