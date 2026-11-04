package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.PrestadorCategoria;
import br.com._hire.projeto_web.entity.PrestadorCategoriaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrestadorCategoriaRepository
        extends JpaRepository<PrestadorCategoria, PrestadorCategoriaId> {

    List<PrestadorCategoria> findByPerfilPrestadorId(UUID perfilPrestadorId);

    List<PrestadorCategoria> findByCategoriaId(Integer categoriaId);

    boolean existsByPerfilPrestadorIdAndCategoriaId(UUID perfilPrestadorId, Integer categoriaId);

    List<PrestadorCategoria> findByCategoriaIdAndPerfilPrestadorAtivoTrue(Integer categoriaId);
}