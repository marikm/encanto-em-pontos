package com.matsumoto.encanto.repository;

import com.matsumoto.encanto.domain.Variacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariacaoRepository extends JpaRepository<Variacao, Integer> {

    List<Variacao> findByProdutoId(Integer produtoId);

}
