package com.matsumoto.encanto.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariacaoResponse {
    private Integer id;
    private String cor;
    private String tamanho;
    private Double peso;
    private Double preco;
    private Integer prazoEmDias;
    private String foto;
    private String nomeProduto;

}
