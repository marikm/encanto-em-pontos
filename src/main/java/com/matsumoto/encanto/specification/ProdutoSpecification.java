package com.matsumoto.encanto.specification;

import com.matsumoto.encanto.domain.Produto;
import org.springframework.data.jpa.domain.Specification;

public class ProdutoSpecification {
    public static Specification<Produto> porCategoria(Integer categoriaId) {
        return (root, query, cb) -> {
            var joinCategoria = root.join("categoria");
            return cb.equal(joinCategoria.get("id"),categoriaId);
        };
    }

    public static Specification<Produto> porCor(String cor) {
        return (root, query, cb) -> {
            var joinVariacao = root.join("variacoes");
            var joinCor = joinVariacao.join("cor");
            return cb.equal(joinCor.get("nome"),cor);
        };
    }

    public static Specification<Produto> precoMinimo(Double precoMin) {
        return (root, query, cb) -> {
            return cb.greaterThanOrEqualTo(root.join("variacoes").get("preco"), precoMin);
        };
    }

    public static Specification<Produto> precoMaximo(Double precoMax) {
        return (root, query, cb) -> {
            return cb.lessThanOrEqualTo(root.join("variacoes").get("preco"), precoMax);
        };
    }

    public static Specification<Produto> porBusca(String busca) {
        return (root, query, cb) -> {
             return cb.like(root.get("nome"), "%"+busca+"%");
        };
    }
}
