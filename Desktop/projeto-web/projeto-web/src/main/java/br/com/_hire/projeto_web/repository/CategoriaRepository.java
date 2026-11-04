package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByNome(String nome);

    boolean existsByNome(String nome);

    List<Categoria> findByCategoriaPaiIsNull();

    List<Categoria> findByCategoriaPaiId(Integer categoriaPaiId);
}