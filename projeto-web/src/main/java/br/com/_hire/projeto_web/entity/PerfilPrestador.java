package br.com._hire.projeto_web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "perfil_prestador")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PerfilPrestador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_perfil_prestador")
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false)
    private Boolean verificadoAntecedentes = false;

    @Column(nullable = false)
    private Boolean verificadoTecnico = false;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitudeBase;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitudeBase;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer raioAtendimentoKm;

}
