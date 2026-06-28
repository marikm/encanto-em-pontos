package com.matsumoto.encanto.service;

import com.matsumoto.encanto.domain.Categoria;
import com.matsumoto.encanto.domain.Produto;
import com.matsumoto.encanto.dto.CategoriaRequest;
import com.matsumoto.encanto.dto.CategoriaResponse;
import com.matsumoto.encanto.exceptions.CategoriaNaoEncontradaException;
import com.matsumoto.encanto.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaResponse criar(CategoriaRequest request) {
        return toResponse(categoriaRepository.save(new Categoria(null, request.getNome(), new ArrayList<Produto>())));
    }

    public List<CategoriaResponse> listarTodos() {
        return categoriaRepository.findAll().
                stream().map(c -> toResponse(c)).collect(Collectors.toList());
    }

    public CategoriaResponse buscarPorId(Integer id) {
        return toResponse(categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException("Categoria não encontrada! O ID " + id + " não existe no catálogo do ateliê.")));
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome());
    }
}
