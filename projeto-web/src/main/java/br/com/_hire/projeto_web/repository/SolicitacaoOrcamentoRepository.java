// versão provisória, apagar quando santana e daniel colocarem a deles

package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.SolicitacaoOrcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SolicitacaoOrcamentoRepository extends JpaRepository<SolicitacaoOrcamento, UUID> {
}