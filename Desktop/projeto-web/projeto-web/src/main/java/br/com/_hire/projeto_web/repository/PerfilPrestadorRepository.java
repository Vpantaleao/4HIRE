package br.com._hire.projeto_web.repository;

import br.com._hire.projeto_web.entity.PerfilPrestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilPrestadorRepository extends JpaRepository<PerfilPrestador, UUID> {

    Optional<PerfilPrestador> findByUsuarioId(UUID usuarioId);

    boolean existsByUsuarioId(UUID usuarioId);

    List<PerfilPrestador> findByAtivoTrue();
}