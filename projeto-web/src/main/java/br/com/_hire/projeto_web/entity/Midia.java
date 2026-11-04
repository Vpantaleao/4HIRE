package br.com._hire.projeto_web.entity;

import br.com._hire.projeto_web.enums.TipoMidia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Arquivo anexado a um (e somente um) destes donos: perfil do prestador,
 * orçamento ou mensagem. As três FKs são anuláveis; qual delas é usada
 * depende do {@link TipoMidia} (regra validada no service).
 */
@Entity
@Table(name = "midia")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Midia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_midia")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil_prestador")
    private PerfilPrestador perfilPrestador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orcamento")
    private SolicitacaoOrcamento orcamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mensagem")
    private Mensagem mensagem;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String urlArquivo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMidia tipoMidia;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void aoCriar() {
        this.criadoEm = LocalDateTime.now();
    }
}
