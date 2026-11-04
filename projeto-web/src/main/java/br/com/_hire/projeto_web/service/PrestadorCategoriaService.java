package br.com._hire.projeto_web.service;

import br.com._hire.projeto_web.dto.PrestadorCategoriaCreateDTO;
import br.com._hire.projeto_web.dto.PrestadorCategoriaResponseDTO;
import br.com._hire.projeto_web.dto.PrestadorCategoriaUpdateDTO;
import br.com._hire.projeto_web.entity.Categoria;
import br.com._hire.projeto_web.entity.PerfilPrestador;
import br.com._hire.projeto_web.entity.PrestadorCategoria;
import br.com._hire.projeto_web.entity.PrestadorCategoriaId;
import br.com._hire.projeto_web.exception.ConflitoException;
import br.com._hire.projeto_web.exception.RecursoNaoEncontradoException;
import br.com._hire.projeto_web.repository.CategoriaRepository;
import br.com._hire.projeto_web.repository.PerfilPrestadorRepository;
import br.com._hire.projeto_web.repository.PrestadorCategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrestadorCategoriaService {

    private final PrestadorCategoriaRepository prestadorCategoriaRepository;
    private final PerfilPrestadorRepository perfilPrestadorRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional
    public PrestadorCategoriaResponseDTO vincular(PrestadorCategoriaCreateDTO dto) {
        PerfilPrestador perfil = perfilPrestadorRepository.findById(dto.perfilPrestadorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Prestador não encontrado: " + dto.perfilPrestadorId()));
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada: " + dto.categoriaId()));

        if (prestadorCategoriaRepository.existsByPerfilPrestadorIdAndCategoriaId(perfil.getId(), categoria.getId())) {
            throw new ConflitoException("O prestador já está vinculado a esta categoria");
        }

        PrestadorCategoria vinculo = new PrestadorCategoria();
        vinculo.setId(new PrestadorCategoriaId(perfil.getId(), categoria.getId()));
        vinculo.setPerfilPrestador(perfil);
        vinculo.setCategoria(categoria);
        vinculo.setPrecoBaseCategoria(dto.precoBaseCategoria());
        return PrestadorCategoriaResponseDTO.de(prestadorCategoriaRepository.save(vinculo));
    }

    @Transactional(readOnly = true)
    public PrestadorCategoriaResponseDTO buscar(UUID perfilPrestadorId, Integer categoriaId) {
        return PrestadorCategoriaResponseDTO.de(buscarEntidade(perfilPrestadorId, categoriaId));
    }

    @Transactional(readOnly = true)
    public List<PrestadorCategoriaResponseDTO> listarPorPrestador(UUID perfilPrestadorId) {
        if (!perfilPrestadorRepository.existsById(perfilPrestadorId)) {
            throw new RecursoNaoEncontradoException("Prestador não encontrado: " + perfilPrestadorId);
        }
        return prestadorCategoriaRepository.findByPerfilPrestadorId(perfilPrestadorId).stream()
                .map(PrestadorCategoriaResponseDTO::de).toList();
    }

    @Transactional(readOnly = true)
    public List<PrestadorCategoriaResponseDTO> listarPorCategoria(Integer categoriaId, boolean apenasAtivos) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new RecursoNaoEncontradoException("Categoria não encontrada: " + categoriaId);
        }
        List<PrestadorCategoria> vinculos = apenasAtivos
                ? prestadorCategoriaRepository.findByCategoriaIdAndPerfilPrestadorAtivoTrue(categoriaId)
                : prestadorCategoriaRepository.findByCategoriaId(categoriaId);
        return vinculos.stream().map(PrestadorCategoriaResponseDTO::de).toList();
    }

    @Transactional
    public PrestadorCategoriaResponseDTO atualizarPreco(UUID perfilPrestadorId, Integer categoriaId,
                                                        PrestadorCategoriaUpdateDTO dto) {
        PrestadorCategoria vinculo = buscarEntidade(perfilPrestadorId, categoriaId);
        vinculo.setPrecoBaseCategoria(dto.precoBaseCategoria());
        return PrestadorCategoriaResponseDTO.de(prestadorCategoriaRepository.save(vinculo));
    }

    @Transactional
    public void desvincular(UUID perfilPrestadorId, Integer categoriaId) {
        prestadorCategoriaRepository.delete(buscarEntidade(perfilPrestadorId, categoriaId));
    }

    PrestadorCategoria buscarEntidade(UUID perfilPrestadorId, Integer categoriaId) {
        return prestadorCategoriaRepository.findById(new PrestadorCategoriaId(perfilPrestadorId, categoriaId))
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Vínculo não encontrado entre o prestador " + perfilPrestadorId
                                + " e a categoria " + categoriaId));
    }
}
