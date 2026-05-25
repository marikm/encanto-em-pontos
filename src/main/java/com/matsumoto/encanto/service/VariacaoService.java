package com.matsumoto.encanto.service;

import com.matsumoto.encanto.domain.Produto;
import com.matsumoto.encanto.domain.Variacao;
import com.matsumoto.encanto.dto.VariacaoRequest;
import com.matsumoto.encanto.dto.VariacaoResponse;
import com.matsumoto.encanto.exceptions.ProdutoNaoEncontradoException;
import com.matsumoto.encanto.exceptions.VariacaoNaoEncontradaException;
import com.matsumoto.encanto.repository.ProdutoRepository;
import com.matsumoto.encanto.repository.VariacaoRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;

@Service
public class VariacaoService {

    private final VariacaoRepository variacaoRepository;
    private final ProdutoRepository produtoRepository;

    public VariacaoService(VariacaoRepository variacaoRepository, ProdutoRepository produtoRepository) {
        this.variacaoRepository = variacaoRepository;
        this.produtoRepository = produtoRepository;
    }

    public VariacaoResponse criar(Integer produtoId, VariacaoRequest variacaoRequest) {
        // busca o produto pelo id
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado! O ID " + produtoId + " não existe no catálogo do ateliê."));
        //  monta a variação com os dados do request
        Variacao variacao = new Variacao();
        variacao.setCor(variacaoRequest.getCor());
        variacao.setTamanho(variacaoRequest.getTamanho());
        variacao.setPeso(variacaoRequest.getPeso());
        variacao.setPreco(variacaoRequest.getPreco());
        variacao.setPrazoEmDias(variacaoRequest.getPrazoEmDias());
        variacao.setFoto(variacaoRequest.getFoto());
        variacao.setProduto(produto);

        Variacao variacaoSalva = variacaoRepository.save(variacao);
        return toResponse(variacaoSalva);

    }
    public VariacaoResponse atualizar(Integer id, VariacaoRequest request) {
        Variacao variacao = variacaoRepository.findById(id)
                .orElseThrow(() -> new VariacaoNaoEncontradaException("Variação não encontrada! O ID " + id + " não existe no catálogo do ateliê."));

        variacao.setCor(request.getCor());
        variacao.setTamanho(request.getTamanho());
        variacao.setPeso(request.getPeso());
        variacao.setPreco(request.getPreco());
        variacao.setPrazoEmDias(request.getPrazoEmDias());
        variacao.setFoto(request.getFoto());

        Variacao variacaoSalva = variacaoRepository.save(variacao);
        return toResponse(variacaoSalva);
    }

    public void deletar(Integer id) {
        Variacao variacao = variacaoRepository.findById(id)
                .orElseThrow(() -> new VariacaoNaoEncontradaException("Variação não encontrada! O ID " + id + " não existe no catálogo do ateliê."));
        variacaoRepository.delete(variacao);
    }

    private VariacaoResponse toResponse(Variacao variacao) {
        Integer id = variacao.getId();
        String cor = variacao.getCor();
        String tamanho = variacao.getTamanho();
        Double peso = variacao.getPeso();
        Double preco = variacao.getPreco();
        Integer prazoEmDias = variacao.getPrazoEmDias();
        String foto = variacao.getFoto();
        String nomeProduto = variacao.getProduto().getNome();

        VariacaoResponse variacaoResponse = new VariacaoResponse(id, cor, tamanho, peso, preco, prazoEmDias, foto, nomeProduto );
        return variacaoResponse;
    }

}
