package com.matsumoto.encanto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponse {
    private Integer id;
    private String nome;
    private String descricao;
    private String foto;
    private String categoriaNome;
    private List<String> materiaisNomes;
}
