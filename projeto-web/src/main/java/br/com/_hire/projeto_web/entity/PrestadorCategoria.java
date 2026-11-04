package br.com._hire.projeto_web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "prestador_categoria")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PrestadorCategoria {

    @EmbeddedId
    private PrestadorCategoriaId id = new PrestadorCategoriaId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("perfilPrestadorId")
    @JoinColumn(name = "id_perfil_prestador")
    private PerfilPrestador perfilPrestador;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoriaId")
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @NotNull
    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private BigDecimal precoBaseCategoria;
}