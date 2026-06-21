package com.matsumoto.encanto.repository;

import com.matsumoto.encanto.domain.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

    Optional<Pessoa> findByEmail(String email);
    Boolean existsByCpf(String cpf);
}
