package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.dto.EnderecoRequest;
import com.matsumoto.encanto.dto.EnderecoResponse;
import com.matsumoto.encanto.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes/{pessoaId}/enderecos")
public class EnderecoController {
    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @PostMapping
    public ResponseEntity<EnderecoResponse> adicionar (@PathVariable Integer pessoaId,
            @RequestBody @Valid EnderecoRequest enderecoRequest) {

        EnderecoResponse enderecoResponse = enderecoService.adicionar(pessoaId, enderecoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoResponse);
    }

    @GetMapping
    public ResponseEntity<List<EnderecoResponse>> listar (@PathVariable Integer pessoaId) {
        List<EnderecoResponse> enderecos = enderecoService.listarPorPessoa(pessoaId);
        return ResponseEntity.status(HttpStatus.OK).body(enderecos);
    }

    @DeleteMapping("/{enderecoId}")
    public ResponseEntity<Void> remover (@PathVariable Integer enderecoId,
                                                     @PathVariable Integer pessoaId) {
        enderecoService.remover(enderecoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
