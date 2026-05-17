package com.matsumoto.encanto.repository;

import com.matsumoto.encanto.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
}
