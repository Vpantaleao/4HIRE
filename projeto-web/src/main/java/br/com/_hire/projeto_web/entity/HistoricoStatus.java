package br.com._hire.projeto_web.entity;

import br.com._hire.projeto_web.enums.StatusOrcamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historico_status")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_historico")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orcamento", nullable = false)
    private SolicitacaoOrcamento orcamento;

    @Enumerated(EnumType.STRING)
    private StatusOrcamento statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrcamento statusNovo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alterado_por", nullable = false)
    private Usuario alteradoPor;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAlteracao;

    @PrePersist
    void aoCriar() {
        this.dataAlteracao = LocalDateTime.now();
    }
}
