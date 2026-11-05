package br.com._hire.projeto_web.controller;

import br.com._hire.projeto_web.dto.HistoricoStatusResponseDTO;
import br.com._hire.projeto_web.dto.OrcamentoAgendamentoDTO;
import br.com._hire.projeto_web.dto.OrcamentoCreateDTO;
import br.com._hire.projeto_web.dto.OrcamentoResponseDTO;
import br.com._hire.projeto_web.dto.OrcamentoTransicaoDTO;
import br.com._hire.projeto_web.dto.OrcamentoValoresDTO;
import br.com._hire.projeto_web.service.SolicitacaoOrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orcamentos")
@RequiredArgsConstructor
public class SolicitacaoOrcamentoController {

    private final SolicitacaoOrcamentoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrcamentoResponseDTO criar(@Valid @RequestBody OrcamentoCreateDTO dto) {
        return service.criar(dto);
    }

    @GetMapping("/{id}")
    public OrcamentoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<OrcamentoResponseDTO> porCliente(@PathVariable UUID clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @GetMapping("/prestador/{perfilPrestadorId}")
    public List<OrcamentoResponseDTO> porPrestador(@PathVariable UUID perfilPrestadorId) {
        return service.listarPorPrestador(perfilPrestadorId);
    }

    @GetMapping("/{id}/historico")
    public List<HistoricoStatusResponseDTO> historico(@PathVariable UUID id) {
        return service.historico(id);
    }

    @PatchMapping("/{id}/orcar")
    public OrcamentoResponseDTO orcar(@PathVariable UUID id, @Valid @RequestBody OrcamentoValoresDTO dto) {
        return service.orcar(id, dto);
    }

    @PatchMapping("/{id}/aceitar")
    public OrcamentoResponseDTO aceitar(@PathVariable UUID id, @Valid @RequestBody OrcamentoTransicaoDTO dto) {
        return service.aceitar(id, dto);
    }

    @PatchMapping("/{id}/agendar")
    public OrcamentoResponseDTO agendar(@PathVariable UUID id, @Valid @RequestBody OrcamentoAgendamentoDTO dto) {
        return service.agendar(id, dto);
    }

    @PatchMapping("/{id}/iniciar")
    public OrcamentoResponseDTO iniciar(@PathVariable UUID id, @Valid @RequestBody OrcamentoTransicaoDTO dto) {
        return service.iniciar(id, dto);
    }

    @PatchMapping("/{id}/concluir")
    public OrcamentoResponseDTO concluir(@PathVariable UUID id, @Valid @RequestBody OrcamentoTransicaoDTO dto) {
        return service.concluir(id, dto);
    }

    @PatchMapping("/{id}/cancelar")
    public OrcamentoResponseDTO cancelar(@PathVariable UUID id, @Valid @RequestBody OrcamentoTransicaoDTO dto) {
        return service.cancelar(id, dto);
    }
}
