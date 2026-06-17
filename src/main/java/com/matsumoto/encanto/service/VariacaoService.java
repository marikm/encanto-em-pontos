package com.matsumoto.encanto.service;

import com.matsumoto.encanto.domain.Cor;
import com.matsumoto.encanto.domain.Produto;
import com.matsumoto.encanto.domain.Variacao;
import com.matsumoto.encanto.dto.ProdutoResponse;
import com.matsumoto.encanto.dto.VariacaoRequest;
import com.matsumoto.encanto.dto.VariacaoResponse;
import com.matsumoto.encanto.exceptions.CorNaoEncontradaException;
import com.matsumoto.encanto.exceptions.ProdutoNaoEncontradoException;
import com.matsumoto.encanto.exceptions.VariacaoNaoEncontradaException;
import com.matsumoto.encanto.repository.CorRepository;
import com.matsumoto.encanto.repository.ProdutoRepository;
import com.matsumoto.encanto.repository.VariacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class VariacaoService {

    private final VariacaoRepository variacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final CorRepository corRepository;

    public VariacaoService(VariacaoRepository variacaoRepository, ProdutoRepository produtoRepository, CorRepository corRepository) {
        this.variacaoRepository = variacaoRepository;
        this.produtoRepository = produtoRepository;
        this.corRepository = corRepository;
    }

    public VariacaoResponse criar(Integer produtoId, VariacaoRequest variacaoRequest) {
        // busca o produto pelo id
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado! O ID " + produtoId + " não existe no catálogo do ateliê."));
        Cor cor = corRepository.findById(variacaoRequest.getCorId())
                .orElseThrow(() -> new CorNaoEncontradaException("Cor não encontrada! O ID " + variacaoRequest.getCorId() + " não existe no catálogo do ateliê."));
        //  monta a variação com os dados do request
        Variacao variacao = new Variacao();
        variacao.setCor(cor);
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
        Cor cor = corRepository.findById(request.getCorId())
                .orElseThrow(() -> new CorNaoEncontradaException("Cor não encontrada! O ID " + request.getCorId() + " não existe no catálogo do ateliê."));
        variacao.setCor(cor);
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

    public VariacaoResponse atualizarFoto(Integer id, String url) {
        Variacao variacao = variacaoRepository.findById(id)
                .orElseThrow(() -> new VariacaoNaoEncontradaException("Variação não encontrada! O ID " + id + " não existe no catálogo do ateliê."));
        variacao.setFoto(url);
        Variacao variacaoSalva = variacaoRepository.save(variacao);
        return toResponse(variacaoSalva);
    }

    private VariacaoResponse toResponse(Variacao variacao) {
        Integer id = variacao.getId();
        String cor = variacao.getCor().getNome();
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
