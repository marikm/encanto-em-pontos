package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.dto.PessoaRequest;
import com.matsumoto.encanto.dto.PessoaResponse;
import com.matsumoto.encanto.service.PessoaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/clientes")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<PessoaResponse> cadastrar(@RequestBody @Valid PessoaRequest request) {
        PessoaResponse pessoaResponse = pessoaService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Integer id) {
        PessoaResponse pessoaResponse = pessoaService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(pessoaResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponse> atualizar(@PathVariable Integer id, @RequestBody @Valid PessoaRequest request) {
        PessoaResponse pessoaResponse = pessoaService.atualizar(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(pessoaResponse);
    }
}