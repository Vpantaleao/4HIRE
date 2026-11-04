package br.com._hire.projeto_web.controller;

import br.com._hire.projeto_web.dto.PrestadorCategoriaCreateDTO;
import br.com._hire.projeto_web.dto.PrestadorCategoriaResponseDTO;
import br.com._hire.projeto_web.dto.PrestadorCategoriaUpdateDTO;
import br.com._hire.projeto_web.service.PrestadorCategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prestador-categorias")
@RequiredArgsConstructor
public class PrestadorCategoriaController {

    private final PrestadorCategoriaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrestadorCategoriaResponseDTO vincular(@Valid @RequestBody PrestadorCategoriaCreateDTO dto) {
        return service.vincular(dto);
    }

    @GetMapping("/prestador/{perfilPrestadorId}")
    public List<PrestadorCategoriaResponseDTO> porPrestador(@PathVariable UUID perfilPrestadorId) {
        return service.listarPorPrestador(perfilPrestadorId);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<PrestadorCategoriaResponseDTO> porCategoria(
            @PathVariable Integer categoriaId,
            @RequestParam(defaultValue = "true") boolean apenasAtivos) {
        return service.listarPorCategoria(categoriaId, apenasAtivos);
    }

    @GetMapping("/prestador/{perfilPrestadorId}/categoria/{categoriaId}")
    public PrestadorCategoriaResponseDTO buscar(@PathVariable UUID perfilPrestadorId,
                                                @PathVariable Integer categoriaId) {
        return service.buscar(perfilPrestadorId, categoriaId);
    }

    @PutMapping("/prestador/{perfilPrestadorId}/categoria/{categoriaId}")
    public PrestadorCategoriaResponseDTO atualizarPreco(@PathVariable UUID perfilPrestadorId,
                                                        @PathVariable Integer categoriaId,
                                                        @Valid @RequestBody PrestadorCategoriaUpdateDTO dto) {
        return service.atualizarPreco(perfilPrestadorId, categoriaId, dto);
    }

    @DeleteMapping("/prestador/{perfilPrestadorId}/categoria/{categoriaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desvincular(@PathVariable UUID perfilPrestadorId, @PathVariable Integer categoriaId) {
        service.desvincular(perfilPrestadorId, categoriaId);
    }
}
