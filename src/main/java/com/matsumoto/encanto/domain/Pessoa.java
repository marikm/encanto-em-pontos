package com.matsumoto.encanto.domain;

import com.matsumoto.encanto.domain.enums.PerfilAcesso;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pessoas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include

    private Integer id;

    @NotBlank
    private String nome;

    @Column(unique = true)
    @NotBlank
    private String cpf
            ;
    @Column(unique = true)
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @NotNull
    private Date dataNascimento;
    private String celular;

    @Enumerated(EnumType.STRING)
    private PerfilAcesso perfilAcesso;

    // Relacionamento 1:N -> Uma pessoa possui muitos enderecos
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos;
}
