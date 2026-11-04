package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.ChatConversa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatConversaRepository extends JpaRepository<ChatConversa, UUID> {

    Optional<ChatConversa> findByClienteIdAndPerfilPrestadorId(UUID clienteId, UUID perfilPrestadorId);

    boolean existsByClienteIdAndPerfilPrestadorId(UUID clienteId, UUID perfilPrestadorId);

    List<ChatConversa> findByClienteId(UUID clienteId);

    List<ChatConversa> findByPerfilPrestadorId(UUID perfilPrestadorId);
}