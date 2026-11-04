package br.com._hire.projeto_web.controller;

import br.com._hire.projeto_web.dto.PerfilPrestadorCreateDTO;
import br.com._hire.projeto_web.dto.PerfilPrestadorResponseDTO;
import br.com._hire.projeto_web.dto.PerfilPrestadorUpdateDTO;
import br.com._hire.projeto_web.dto.PerfilPrestadorVerificacaoDTO;
import br.com._hire.projeto_web.service.PerfilPrestadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prestadores")
@RequiredArgsConstructor
public class PerfilPrestadorController {

    private final PerfilPrestadorService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PerfilPrestadorResponseDTO criar(@Valid @RequestBody PerfilPrestadorCreateDTO dto) {
        return service.criar(dto);
    }

    @GetMapping
    public List<PerfilPrestadorResponseDTO> listar(@RequestParam(defaultValue = "true") boolean apenasAtivos) {
        return service.listar(apenasAtivos);
    }

    @GetMapping("/{id}")
    public PerfilPrestadorResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public PerfilPrestadorResponseDTO porUsuario(@PathVariable UUID usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    @PutMapping("/{id}")
    public PerfilPrestadorResponseDTO atualizar(@PathVariable UUID id,
                                                @Valid @RequestBody PerfilPrestadorUpdateDTO dto) {
        return service.atualizar(id, dto);
    }

    @PatchMapping("/{id}/ativar")
    public PerfilPrestadorResponseDTO ativar(@PathVariable UUID id) {
        return service.alterarAtivo(id, true);
    }

    @PatchMapping("/{id}/desativar")
    public PerfilPrestadorResponseDTO desativar(@PathVariable UUID id) {
        return service.alterarAtivo(id, false);
    }

    @PatchMapping("/{id}/verificacao")
    public PerfilPrestadorResponseDTO verificacao(@PathVariable UUID id,
                                                  @Valid @RequestBody PerfilPrestadorVerificacaoDTO dto) {
        return service.atualizarVerificacao(id, dto);
    }
}
