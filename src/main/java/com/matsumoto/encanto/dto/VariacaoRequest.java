package com.matsumoto.encanto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariacaoRequest {
    private String cor;
    private String tamanho;
    @Positive
    private Double peso;
    @Positive
    @NotNull
    private Double preco;
    @Positive
    private Integer prazoEmDias;
    private String foto;

}
