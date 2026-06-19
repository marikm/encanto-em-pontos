package com.matsumoto.encanto.dto;

import com.matsumoto.encanto.domain.enums.PerfilAcesso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PessoaRequest {
    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    private String email;

    @NotBlank
    private String senha;
    private Date dataNascimento;
    private String celular;
    @NotNull
    private PerfilAcesso perfil;
}
