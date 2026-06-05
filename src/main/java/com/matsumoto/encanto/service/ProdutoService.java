package com.matsumoto.encanto.service;

import com.matsumoto.encanto.domain.Categoria;
import com.matsumoto.encanto.domain.Material;
import com.matsumoto.encanto.domain.Produto;
import com.matsumoto.encanto.dto.ProdutoRequest;
import com.matsumoto.encanto.dto.ProdutoResponse;
import com.matsumoto.encanto.exceptions.CategoriaNaoEncontradaException;
import com.matsumoto.encanto.exceptions.ProdutoNaoEncontradoException;
import com.matsumoto.encanto.repository.CategoriaRepository;
import com.matsumoto.encanto.repository.MaterialRepository;
import com.matsumoto.encanto.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
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
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId()).orElseThrow(
                () -> new CategoriaNaoEncontradaException("Categoria não encontrada! O ID " + request.getCategoriaId() + " não existe no catálogo do ateliê. "));
        Set<Integer> ids = request.getMateriaisIds() != null ?
                request.getMateriaisIds() : new HashSet<>();
        Set<Material> materiais = new HashSet<>(materialRepository.findAllById(ids));
        // Montar o Produto
        Produto produto = new Produto();
        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setFoto(request.getFoto());
        produto.setCategoria(categoria);
        produto.setMateriais(materiais);

        Produto produtoSalvo = produtoRepository.save(produto);
        return toResponse(produtoSalvo);
    }
    public List<ProdutoResponse> listarTodos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProdutoResponse buscarPorId(Integer id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado! O ID " + id + " não existe no catálogo do ateliê."));
        return toResponse(produto);
    }

    public ProdutoResponse atualizar(Integer id, ProdutoRequest request) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado! O ID " + id + " não existe no catálogo do ateliê."));
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId()).orElseThrow(
                () -> new CategoriaNaoEncontradaException("Categoria não encontrada! O ID " + request.getCategoriaId() + " não existe no catálogo do ateliê. "));
        Set<Integer> ids = request.getMateriaisIds() != null ?
                        request.getMateriaisIds() : new HashSet<>();
        Set<Material> materiais = new HashSet<>(materialRepository.findAllById(ids));
        //
        // Atualizando dados
        //
        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setCategoria(categoria);
        produto.setMateriais(materiais);
        produto.setFoto(request.getFoto());
        Produto produtoSalvo = produtoRepository.save(produto);

        return toResponse(produtoSalvo);
    }

    public void deletar(Integer id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado! O ID " + id + " não existe no catálogo do ateliê."));
        produtoRepository.delete(produto);
    }

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
