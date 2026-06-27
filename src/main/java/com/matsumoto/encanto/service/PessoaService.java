package com.matsumoto.encanto.service;

import com.matsumoto.encanto.domain.Pessoa;
import com.matsumoto.encanto.domain.enums.PerfilAcesso;
import com.matsumoto.encanto.dto.PessoaRequest;
import com.matsumoto.encanto.dto.PessoaResponse;
import com.matsumoto.encanto.exceptions.CpfJaCadastradoException;
import com.matsumoto.encanto.exceptions.PessoaNaoEncontradaException;
import com.matsumoto.encanto.repository.PessoaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PessoaService implements UserDetailsService {
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    // construtor
    public PessoaService(PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder) {
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PessoaResponse cadastrar(PessoaRequest request) {
        // 1. verificar se CPF já existe
        if (pessoaRepository.existsByCpf(request.getCpf())){
            throw new CpfJaCadastradoException("CPF já cadastrado! O CPF " + request.getCpf() + " já existe.");
        }
        // 2. montar entidade Pessoa
        Pessoa pessoa = new Pessoa();
        return getPessoaResponse(request, pessoa);
    }

    public PessoaResponse buscarPorId(Integer id) {
        // buscar ou lançar exception
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException("Pessoa não encontrada! O id" + id + " não foi encontrado!"));

        return toResponse(pessoa);
    }

    public PessoaResponse atualizar(Integer id, PessoaRequest
            request) {
        // buscar existente, atualizar campos, salvar
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException("Pessoa não encontrada! O id" + id + " não foi encontrado!"));
        return getPessoaResponse(request, pessoa);
    }

    private PessoaResponse getPessoaResponse(PessoaRequest request, Pessoa pessoa) {
        pessoa.setNome(request.getNome());
        pessoa.setCpf(request.getCpf());
        pessoa.setEmail(request.getEmail());
        pessoa.setCelular(request.getCelular());
        pessoa.setDataNascimento(request.getDataNascimento());
        pessoa.setPerfilAcesso(request.getPerfil());
        pessoa.setSenha(passwordEncoder.encode(request.getSenha()));

        return toResponse(pessoaRepository.save(pessoa));
    }

    private PessoaResponse toResponse(Pessoa pessoa) {
        String nome = pessoa.getNome();
        String cpf = pessoa.getCpf();
        String email = pessoa.getEmail();
        String celular = pessoa.getCelular();
        Date dataNascimento = pessoa.getDataNascimento();
        PerfilAcesso perfil = pessoa.getPerfilAcesso();
        // mapear entidade → DTO
        return new PessoaResponse(nome, cpf, email, dataNascimento, celular, perfil);



    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return pessoaRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("E-mail não encontrado"));
    }
}
