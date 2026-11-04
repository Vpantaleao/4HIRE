package br.com._hire.projeto_web.controller;

import br.com._hire.projeto_web.dto.CategoriaCreateDTO;
import br.com._hire.projeto_web.dto.CategoriaResponseDTO;
import br.com._hire.projeto_web.dto.CategoriaUpdateDTO;
import br.com._hire.projeto_web.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO criar(@Valid @RequestBody CategoriaCreateDTO dto) {
        return service.criar(dto);
    }

    @GetMapping
    public List<CategoriaResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/raizes")
    public List<CategoriaResponseDTO> raizes() {
        return service.listarRaizes();
    }

    @GetMapping("/{id}")
    public CategoriaResponseDTO buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/{id}/subcategorias")
    public List<CategoriaResponseDTO> subcategorias(@PathVariable Integer id) {
        return service.listarSubcategorias(id);
    }

    @PutMapping("/{id}")
    public CategoriaResponseDTO atualizar(@PathVariable Integer id,
                                          @Valid @RequestBody CategoriaUpdateDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
