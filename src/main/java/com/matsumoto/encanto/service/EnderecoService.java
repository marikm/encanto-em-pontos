package com.matsumoto.encanto.service;

import com.matsumoto.encanto.domain.Endereco;
import com.matsumoto.encanto.domain.Pessoa;
import com.matsumoto.encanto.dto.EnderecoRequest;
import com.matsumoto.encanto.dto.EnderecoResponse;
import com.matsumoto.encanto.exceptions.EnderecoNaoEncontradoException;
import com.matsumoto.encanto.exceptions.PessoaNaoEncontradaException;
import com.matsumoto.encanto.repository.EnderecoRepository;
import com.matsumoto.encanto.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final PessoaRepository pessoaRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, PessoaRepository pessoaRepository) {
        this.enderecoRepository = enderecoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public EnderecoResponse adicionar(Integer pessoaId, EnderecoRequest enderecoRequest) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new PessoaNaoEncontradaException("Pessoa não encontrada! O id" + pessoaId + " não foi encontrado!"));

        Endereco endereco = new Endereco();
        return getEnderecoResponse(enderecoRequest, pessoa, endereco);
    }

    public List<EnderecoResponse> listarPorPessoa(Integer pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new PessoaNaoEncontradaException("Pessoa não encontrada! O id" + pessoaId + " não foi encontrado!"));

        List<Endereco> enderecos = enderecoRepository.findByPessoaId(pessoaId);

        List<EnderecoResponse> enderecoResponses = new ArrayList<>();

        return enderecoRepository.findByPessoaId(pessoaId)
                .stream()
                .map(e -> toResponse(e, e.getPessoa()))
                .collect(Collectors.toList());
    }

    public void remover(Integer enderecoId) {
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EnderecoNaoEncontradoException("Endereço não encontrado! O id " + enderecoId + " não foi encontrado!"));
        enderecoRepository.delete(endereco);

    }

    private EnderecoResponse getEnderecoResponse(EnderecoRequest enderecoRequest, Pessoa pessoa, Endereco endereco) {
        endereco.setPessoa(pessoa);
        endereco.setCep(enderecoRequest.getCep());
        endereco.setLogradouro(enderecoRequest.getLogradouro());
        endereco.setNumero(enderecoRequest.getNumero());
        endereco.setComplemento(enderecoRequest.getComplemento());
        endereco.setCidade(enderecoRequest.getCidade());
        endereco.setEstado(enderecoRequest.getEstado());
        endereco.setBairro(enderecoRequest.getBairro());

        return  toResponse(enderecoRepository.save(endereco), pessoa);
    }


    private EnderecoResponse toResponse(Endereco endereco, Pessoa pessoa) {
        EnderecoResponse enderecoResponse = new EnderecoResponse();

        enderecoResponse.setBairro(endereco.getBairro());
        enderecoResponse.setCep(endereco.getCep());
        enderecoResponse.setCidade(endereco.getCidade());
        enderecoResponse.setEstado(endereco.getEstado());
        enderecoResponse.setNumero(endereco.getNumero());
        enderecoResponse.setComplemento(endereco.getComplemento());
        enderecoResponse.setLogradouro(endereco.getLogradouro());
        enderecoResponse.setPessoaNome(pessoa.getNome());

        return enderecoResponse;

    }
}
