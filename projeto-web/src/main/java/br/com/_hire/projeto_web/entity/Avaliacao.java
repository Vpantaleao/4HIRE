package br.com._hire.projeto_web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "avaliacao")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_avaliacao")
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orcamento", nullable = false, unique = true)
    private SolicitacaoOrcamento orcamento;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avaliador", nullable = false)
    private Usuario avaliador;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil_prestador", nullable = false)
    private PerfilPrestador perfilPrestador;

    @NotNull
    @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer nota;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAvaliacao;

    @PrePersist
    void aoCriar() {
        this.dataAvaliacao = LocalDateTime.now();
    }
}