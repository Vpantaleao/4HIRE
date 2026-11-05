package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.Pagamento;
import br.com._hire.projeto_web.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

    List<Pagamento> findByOrcamentoIdOrderByDataCriacaoDesc(UUID orcamentoId);

    boolean existsByOrcamentoIdAndStatusIn(UUID orcamentoId, Collection<StatusPagamento> status);
}
