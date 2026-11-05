package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.Pagamento;
import br.com._hire.projeto_web.entity.SolicitacaoOrcamento;
import br.com._hire.projeto_web.enums.StatusPagamento;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

    List<Pagamento> findByOrcamentoIdOrderByDataCriacaoDesc(UUID orcamentoId);

    boolean existsByOrcamentoIdAndStatusIn(UUID orcamentoId, Collection<StatusPagamento> status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from SolicitacaoOrcamento o where o.id = :id")
    Optional<SolicitacaoOrcamento> buscarOrcamentoParaAtualizar(@Param("id") UUID id);
}
