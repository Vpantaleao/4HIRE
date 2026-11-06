package br.com._hire.projeto_web.controller;

import br.com._hire.projeto_web.dto.MidiaCreateDTO;
import br.com._hire.projeto_web.dto.MidiaResponseDTO;
import br.com._hire.projeto_web.enums.TipoMidia;
import br.com._hire.projeto_web.service.MidiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/midias")
@RequiredArgsConstructor
public class MidiaController {

    private final MidiaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MidiaResponseDTO criar(@Valid @RequestBody MidiaCreateDTO dto) {
        return service.criar(dto);
    }

    @GetMapping("/{id}")
    public MidiaResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/prestador/{perfilPrestadorId}")
    public List<MidiaResponseDTO> porPrestador(@PathVariable UUID perfilPrestadorId,
                                               @RequestParam(required = false) TipoMidia tipo) {
        return service.listarPorPrestador(perfilPrestadorId, tipo);
    }

    @GetMapping("/orcamento/{orcamentoId}")
    public List<MidiaResponseDTO> porOrcamento(@PathVariable UUID orcamentoId) {
        return service.listarPorOrcamento(orcamentoId);
    }

    @GetMapping("/mensagem/{mensagemId}")
    public List<MidiaResponseDTO> porMensagem(@PathVariable UUID mensagemId) {
        return service.listarPorMensagem(mensagemId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
