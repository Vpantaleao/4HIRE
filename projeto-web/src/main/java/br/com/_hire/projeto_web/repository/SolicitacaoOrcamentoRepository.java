// versão provisória, apagar quando santana e daniel colocarem a deles

package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.SolicitacaoOrcamento;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolicitacaoOrcamentoRepository extends JpaRepository<SolicitacaoOrcamento, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from SolicitacaoOrcamento o where o.id = :id")
    Optional<SolicitacaoOrcamento> buscarParaAtualizar(@Param("id") UUID id);
}
