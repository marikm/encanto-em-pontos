package com.matsumoto.encanto.repository;

import com.matsumoto.encanto.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
