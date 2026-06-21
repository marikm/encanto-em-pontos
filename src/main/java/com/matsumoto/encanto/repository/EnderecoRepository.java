package com.matsumoto.encanto.repository;

import com.matsumoto.encanto.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    List<Endereco> findByPessoaId(Integer pessoaId);
}
