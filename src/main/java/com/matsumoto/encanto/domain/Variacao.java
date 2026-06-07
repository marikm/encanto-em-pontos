package com.matsumoto.encanto.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "variacao")
public class Variacao {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cor_id")
    private Cor cor;
    private String tamanho;

    @Positive
    private Double peso;  // em gramas

    @NotNull
    @Positive
    private Double preco;

    @Positive
    private Integer prazoEmDias;
    private String foto;

    // relacionamento com Produto
    @NotNull
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
}
