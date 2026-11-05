package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.HistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, UUID> {

    List<HistoricoStatus> findByOrcamentoIdOrderByDataAlteracaoAsc(UUID orcamentoId);
}
