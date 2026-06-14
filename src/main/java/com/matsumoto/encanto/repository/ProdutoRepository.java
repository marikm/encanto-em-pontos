package com.matsumoto.encanto.repository;

import com.matsumoto.encanto.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
