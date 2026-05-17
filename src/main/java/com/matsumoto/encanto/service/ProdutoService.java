package com.matsumoto.encanto.service;

import com.matsumoto.encanto.domain.Categoria;
import com.matsumoto.encanto.domain.Material;
import com.matsumoto.encanto.domain.Produto;
import com.matsumoto.encanto.dto.ProdutoRequest;
import com.matsumoto.encanto.dto.ProdutoResponse;
import com.matsumoto.encanto.exceptions.ProdutoNaoEncontradoException;
import com.matsumoto.encanto.repository.CategoriaRepository;
import com.matsumoto.encanto.repository.MaterialRepository;
import com.matsumoto.encanto.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MaterialRepository materialRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, MaterialRepository materialRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.materialRepository = materialRepository;
    }

    public ProdutoResponse criar (ProdutoRequest request) {

    }

    public List<ProdutoResponse> listarTodos() {}

    public ProdutoResponse buscarPorId(Integer id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado! O ID " + id + " não existe no catálogo do ateliê."));
        return toResponse(produto);
    }

    public ProdutoResponse atualizar(Integer id, ProdutoRequest request) {}

    public void deletar(Integer id) {}

    private ProdutoResponse toResponse(Produto produto) {
        Integer id = produto.getId();
        String nome = produto.getNome();
        String descricao = produto.getDescricao();
        String foto = produto.getFoto();
        String categoria = produto.getCategoria().getNome();
        List<String> materiais = produto.getMateriais().stream().map(material -> material.getNome()).collect(Collectors.toList());
        ProdutoResponse produtoResponse = new ProdutoResponse(id, nome, descricao, foto, categoria, materiais);
        return produtoResponse;

    }

}
