package br.com._hire.projeto_web.service;

import br.com._hire.projeto_web.dto.CategoriaCreateDTO;
import br.com._hire.projeto_web.dto.CategoriaResponseDTO;
import br.com._hire.projeto_web.dto.CategoriaUpdateDTO;
import br.com._hire.projeto_web.entity.Categoria;
import br.com._hire.projeto_web.exception.ConflitoException;
import br.com._hire.projeto_web.exception.RecursoNaoEncontradoException;
import br.com._hire.projeto_web.repository.CategoriaRepository;
import br.com._hire.projeto_web.repository.PrestadorCategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final PrestadorCategoriaRepository prestadorCategoriaRepository;

    @Transactional
    public CategoriaResponseDTO criar(CategoriaCreateDTO dto) {
        String nome = dto.nome().trim();
        if (categoriaRepository.existsByNome(nome)) {
            throw new ConflitoException("Já existe uma categoria com o nome: " + nome);
        }

        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao(dto.descricao());
        categoria.setCategoriaPai(dto.categoriaPaiId() == null ? null : buscarEntidade(dto.categoriaPaiId()));
        return CategoriaResponseDTO.de(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Integer id) {
        return CategoriaResponseDTO.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaResponseDTO::de).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarRaizes() {
        return categoriaRepository.findByCategoriaPaiIsNull().stream()
                .map(CategoriaResponseDTO::de).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarSubcategorias(Integer categoriaPaiId) {
        buscarEntidade(categoriaPaiId);
        return categoriaRepository.findByCategoriaPaiId(categoriaPaiId).stream()
                .map(CategoriaResponseDTO::de).toList();
    }

    @Transactional
    public CategoriaResponseDTO atualizar(Integer id, CategoriaUpdateDTO dto) {
        Categoria categoria = buscarEntidade(id);

        String nome = dto.nome().trim();
        categoriaRepository.findByNome(nome)
                .filter(outra -> !outra.getId().equals(id))
                .ifPresent(outra -> {
                    throw new ConflitoException("Já existe uma categoria com o nome: " + nome);
                });

        Categoria novoPai = null;
        if (dto.categoriaPaiId() != null) {
            novoPai = buscarEntidade(dto.categoriaPaiId());
            validarSemCiclo(categoria, novoPai);
        }

        categoria.setNome(nome);
        categoria.setDescricao(dto.descricao());
        categoria.setCategoriaPai(novoPai);
        return CategoriaResponseDTO.de(categoriaRepository.save(categoria));
    }

    @Transactional
    public void deletar(Integer id) {
        Categoria categoria = buscarEntidade(id);

        if (!categoriaRepository.findByCategoriaPaiId(id).isEmpty()) {
            throw new ConflitoException("Não é possível excluir: a categoria possui subcategorias");
        }
        if (!prestadorCategoriaRepository.findByCategoriaId(id).isEmpty()) {
            throw new ConflitoException("Não é possível excluir: há prestadores vinculados à categoria");
        }

        try {
            categoriaRepository.delete(categoria);
            categoriaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflitoException("Não é possível excluir: a categoria está em uso");
        }
    }

    Categoria buscarEntidade(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada: " + id));
    }

    // impede que a categoria vire pai de si mesma ou de um de seus ancestrais
    private void validarSemCiclo(Categoria categoria, Categoria novoPai) {
        Categoria atual = novoPai;
        while (atual != null) {
            if (atual.getId().equals(categoria.getId())) {
                throw new ConflitoException("A categoria não pode ser pai de si mesma nem de uma de suas ancestrais");
            }
            atual = atual.getCategoriaPai();
        }
    }
}
