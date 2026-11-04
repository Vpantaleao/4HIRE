package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, UUID> {

    List<Mensagem> findByConversaIdOrderByDataEnvioAsc(UUID conversaId);

    List<Mensagem> findByConversaIdAndRemetenteIdNotAndLidaEmIsNull(UUID conversaId, UUID remetenteId);

    long countByConversaIdAndRemetenteIdNotAndLidaEmIsNull(UUID conversaId, UUID remetenteId);
}