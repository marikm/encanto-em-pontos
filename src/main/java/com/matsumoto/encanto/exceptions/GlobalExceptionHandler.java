package com.matsumoto.encanto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CorNaoEncontradaException.class)
    public ResponseEntity<Object> handleCorNaoEncontrada(CorNaoEncontradaException ex) {
        Map<String, Object> body = criarCorpoResposta(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoriaNaoEncontradaException.class)
    public ResponseEntity<Object> handleCategoriaNaoEncontrada(CategoriaNaoEncontradaException ex) {
        Map<String, Object> body = criarCorpoResposta(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<Object> handleProdutoNaoEncontrado(ProdutoNaoEncontradoException ex) {
        Map<String, Object> body = criarCorpoResposta(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VariacaoNaoEncontradaException.class)
    public ResponseEntity<Object> handleVariacaoNaoEncontradaException(VariacaoNaoEncontradaException ex) {
        Map<String, Object> body = criarCorpoResposta(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PessoaNaoEncontradaException.class)
    public ResponseEntity<Object> handlePessoaNaoEncontradaException(PessoaNaoEncontradaException ex) {
        Map<String, Object> body = criarCorpoResposta(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EnderecoNaoEncontradoException.class)
    public ResponseEntity<Object> handleEnderecoNaoEncontrado(EnderecoNaoEncontradoException ex) {
        Map<String, Object> body = criarCorpoResposta(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<Object> handleCpfJaCadastrado(CpfJaCadastradoException ex) {
        Map<String, Object> body = criarCorpoResposta(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    private Map<String, Object> criarCorpoResposta(HttpStatus status, String mensagem) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("mensagem", mensagem);
        return body;
    }
}

