package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {

    boolean existsByOrcamentoId(UUID orcamentoId);

    List<Avaliacao> findByPerfilPrestadorIdOrderByDataAvaliacaoDesc(UUID perfilPrestadorId);

    long countByPerfilPrestadorId(UUID perfilPrestadorId);

    @Query("select avg(a.nota) from Avaliacao a where a.perfilPrestador.id = :perfilId")
    Double mediaPorPrestador(@Param("perfilId") UUID perfilId);
}