package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.domain.Cor;
import com.matsumoto.encanto.service.CorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/cores")
public class CorController {
    private final CorService corService;

    // Injeção de dependência via construtor
    public CorController(CorService corService) {
        this.corService = corService;
    }

    @GetMapping
    public ResponseEntity<List<Cor>> listarCores() {
        List<Cor> cores = corService.listarCores();
        return ResponseEntity.ok().body(cores);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Cor> buscarCorPorId(@PathVariable Integer id) {
        // O Controller apenas chama o Service e espera o sucesso
        Cor corEncontrada = corService.buscarPorId(id);

        // Se deu tudo certo, devolve a cor na nossa "Bandeja" com o Status 200 OK
        return ResponseEntity.ok(corEncontrada);
    }
}
