package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.domain.Categoria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaController {

    @GetMapping
    public ResponseEntity<Categoria> findAll() {
        Categoria categoria = new Categoria(1, "blusas");
        return ResponseEntity.ok().body(categoria);
    }
}
