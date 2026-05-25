package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.dto.ProdutoRequest;
import com.matsumoto.encanto.dto.ProdutoResponse;
import com.matsumoto.encanto.dto.VariacaoRequest;
import com.matsumoto.encanto.dto.VariacaoResponse;
import com.matsumoto.encanto.repository.VariacaoRepository;
import com.matsumoto.encanto.service.VariacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class VariacaoController {

    private final VariacaoService variacaoService;

    public VariacaoController(VariacaoService variacaoService) {
        this.variacaoService = variacaoService;
    }

    @PostMapping("/produtos/{id}/variacoes")
    public ResponseEntity<VariacaoResponse> criar(@PathVariable Integer id, @RequestBody VariacaoRequest variacaoRequest) {
        VariacaoResponse variacaoResponse = variacaoService.criar(id, variacaoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(variacaoResponse);
    }

    @PutMapping("/variacoes/{id}")
    public ResponseEntity<VariacaoResponse> atualizar(@PathVariable Integer id, @RequestBody VariacaoRequest variacaoRequest) {
        VariacaoResponse variacaoResponse = variacaoService.atualizar(id, variacaoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(variacaoResponse);
    }

    @DeleteMapping("/variacoes/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        variacaoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
