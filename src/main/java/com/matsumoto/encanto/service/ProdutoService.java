package com.matsumoto.encanto.service;

import com.matsumoto.encanto.domain.Categoria;
import com.matsumoto.encanto.domain.Material;
import com.matsumoto.encanto.domain.Produto;
import com.matsumoto.encanto.dto.ProdutoDetalheResponse;
import com.matsumoto.encanto.dto.ProdutoRequest;
import com.matsumoto.encanto.dto.ProdutoResponse;
import com.matsumoto.encanto.dto.VariacaoResponse;
import com.matsumoto.encanto.exceptions.CategoriaNaoEncontradaException;
import com.matsumoto.encanto.exceptions.ProdutoNaoEncontradoException;
import com.matsumoto.encanto.repository.CategoriaRepository;
import com.matsumoto.encanto.repository.MaterialRepository;
import com.matsumoto.encanto.repository.ProdutoRepository;
import com.matsumoto.encanto.specification.ProdutoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<ProdutoResponse> listarTodos(Pageable pageable) {
        Page<Produto> produtos = produtoRepository.findAll(pageable);
        return produtos.map(this::toResponse);
    }

    public ProdutoDetalheResponse buscarPorId(Integer id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado! O ID " + id + " não existe no catálogo do ateliê."));
        return toDetalheResponse(produto);
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

    public ProdutoResponse atualizarFoto(Integer id, String url) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado! O ID " + id + " não existe no catálogo do ateliê."));
        produto.setFoto(url);
        Produto produtoSalvo = produtoRepository.save(produto);
        return toResponse(produtoSalvo);
    }

    private ProdutoResponse toResponse(Produto produto) {
        Integer id = produto.getId();
        String nome = produto.getNome();
        String descricao = produto.getDescricao();
        String foto = produto.getFoto();
        String categoria = produto.getCategoria().getNome();
        List<String> materiais = new ArrayList<>();
        if (produto.getMateriais() != null)  {
            materiais = produto.getMateriais().stream().map(Material::getNome).collect(Collectors.toList());

        }
        return new ProdutoResponse(id, nome, descricao, foto, categoria, materiais);
    }

    private ProdutoDetalheResponse toDetalheResponse(Produto produto) {
        Integer id = produto.getId();
        String nome = produto.getNome();
        String descricao = produto.getDescricao();
        String foto = produto.getFoto();
        String categoria = produto.getCategoria().getNome();
        List<String> materiais = new ArrayList<>();
        if (produto.getMateriais() != null)  {
            materiais = produto.getMateriais().stream().map(Material::getNome).collect(Collectors.toList());

        }
        List<VariacaoResponse> variacoes = new ArrayList<>();
        if (produto.getVariacoes() != null) {
            variacoes = produto.getVariacoes().stream().
                    map(v -> new VariacaoResponse(v.getId(), v.getCor().getNome(), v.getTamanho(), v.getPeso(), v.getPreco(),
                            v.getPrazoEmDias(),v.getFoto(), v.getProduto().getNome())).collect(Collectors.toList());
        }


        return new ProdutoDetalheResponse(id, nome, descricao, foto, categoria, materiais, variacoes);
    }

    public Page<ProdutoResponse> filtrar (Integer categoriaId, String cor, Double precoMin,
                                          Double precoMax, String busca, Pageable pageable) {
        Specification<Produto> spec = (root, query, cb) -> cb.conjunction();
        if (categoriaId != null) {
            spec = spec.and(ProdutoSpecification.porCategoria(categoriaId));
        }

        if (cor != null && !cor.isBlank()) {
            spec = spec.and(ProdutoSpecification.porCor(cor));
        }

        if(precoMin != null) {
            spec = spec.and(ProdutoSpecification.precoMinimo(precoMin));
        }
        if (precoMax != null) {
            spec = spec.and(ProdutoSpecification.precoMaximo(precoMax));
        }
        if (busca != null && !busca.isBlank()){
            spec = spec.and(ProdutoSpecification.porBusca(busca));
        }

        return produtoRepository.findAll(spec, pageable).map(this::toResponse);

    }

}
