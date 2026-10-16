package br.com._hire.projeto_web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_conversa", uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_perfil_prestador"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class ChatConversa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_conversa")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil_prestador", nullable = false)
    private PerfilPrestador perfilPrestador;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    void aoCriar() {
        this.dataCriacao = LocalDateTime.now();
    }
}
