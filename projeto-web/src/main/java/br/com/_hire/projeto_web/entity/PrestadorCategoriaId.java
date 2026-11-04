package br.com._hire.projeto_web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class PrestadorCategoriaId implements Serializable {

    @Column(name = "id_perfil_prestador")
    private UUID perfilPrestadorId;

    @Column(name = "id_categoria")
    private Integer categoriaId;
}