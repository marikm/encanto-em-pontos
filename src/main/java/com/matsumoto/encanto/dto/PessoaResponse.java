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
public class PessoaResponse {

    private String nome;
    private String cpf;
    private String email;
    private Date dataNascimento;
    private String celular;
    private PerfilAcesso perfil;

}
