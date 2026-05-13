package com.matsumoto.encanto.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "produto")
public class Produto {
    @EqualsAndHashCode.Include

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;
    private String foto;

    // Relacionamento
    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoria_id") // Nome da coluna FK na tabela produtos
    private Categoria categoria;

    // Relacionamento N:M -> Tabela de Produto_Materiais
    @ManyToMany
    @JoinTable(
            name = "produto_materiais",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    // Lista de relacao entre produto e materiais
    // set nao permite duplicados
    private Set<Material> materiais = new HashSet<>();

    // Relacionamento 1:N : Um produto possui varias variacoes
   @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Variacao> variacoes = new ArrayList<>();


}


