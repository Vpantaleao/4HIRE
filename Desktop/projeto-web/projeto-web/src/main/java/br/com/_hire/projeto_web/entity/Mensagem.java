package br.com._hire.projeto_web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mensagem")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_mensagem")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conversa", nullable = false)
    private ChatConversa conversa;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_remetente", nullable = false)
    private Usuario remetente;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudoTexto;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataEnvio;

    @Column
    private LocalDateTime lidaEm;

    @PrePersist
    void aoCriar() {
        this.dataEnvio = LocalDateTime.now();
    }
}
