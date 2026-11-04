package br.com._hire.projeto_web.entity;

import br.com._hire.projeto_web.enums.StatusOrcamento;
import br.com._hire.projeto_web.enums.TurnoPreferencia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitacao_orcamento")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SolicitacaoOrcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_orcamento")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil_prestador", nullable = false)
    private PerfilPrestador perfilPrestador;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conversa", nullable = false, unique = true)
    private ChatConversa conversa;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrcamento statusAtual = StatusOrcamento.SOLICITADO;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricaoServico;

    @NotBlank
    @Column(nullable = false)
    private String enderecoTexto;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitudeServico;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitudeServico;

    private LocalDateTime dataAgendamento;

    @Enumerated(EnumType.STRING)
    private TurnoPreferencia turnoPreferencia;

    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private BigDecimal valorVisitaTecnica;

    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private BigDecimal valorMaoDeObra;

    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private BigDecimal taxaPlataforma;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataSolicitacao;

    @PrePersist
    void aoCriar() {
        this.dataSolicitacao = LocalDateTime.now();
    }


    public BigDecimal getValorTotal() {
        return valorOuZero(valorVisitaTecnica)
                .add(valorOuZero(valorMaoDeObra))
                .add(valorOuZero(taxaPlataforma));
    }

    private BigDecimal valorOuZero(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }
}
