package com.matsumoto.encanto.service;

import com.matsumoto.encanto.domain.Cor;
import com.matsumoto.encanto.exceptions.CorNaoEncontradaException;
import com.matsumoto.encanto.repository.CorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorService {
    private final CorRepository corRepository;

    public CorService(CorRepository corRepository) {
        this.corRepository = corRepository;
    }

    public Cor buscarPorId(Integer id) {
        // Vai no banco. Se não achar, lança a NOSSA exceção customizada!
        return corRepository.findById(id)
                .orElseThrow(() -> new CorNaoEncontradaException("Cor não encontrada! O ID " + id + " não existe no catálogo do ateliê."));
    }

    public List<Cor> listarCores() {
        return corRepository.findAll();
    }
}
