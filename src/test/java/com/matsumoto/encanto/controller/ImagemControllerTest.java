package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.domain.Categoria;
import com.matsumoto.encanto.domain.Cor;
import com.matsumoto.encanto.domain.Produto;
import com.matsumoto.encanto.domain.Variacao;
import com.matsumoto.encanto.repository.CategoriaRepository;
import com.matsumoto.encanto.repository.CorRepository;
import com.matsumoto.encanto.repository.ProdutoRepository;
import com.matsumoto.encanto.repository.VariacaoRepository;
import com.matsumoto.encanto.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ImagemControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext context;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final VariacaoRepository variacaoRepository;
    private final CorRepository corRepository;
    private Categoria categoria;
    private Produto produto;
    private Integer produtoId;
    private Cor cor;

    @MockitoBean
    private CloudinaryService cloudinaryService;

    @Autowired
    ImagemControllerTest(WebApplicationContext context, CategoriaRepository categoriaRepository,
                         ProdutoRepository produtoRepository, VariacaoRepository variacaoRepository,
                         CorRepository corRepository) {
        this.context = context;
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.variacaoRepository = variacaoRepository;
        this.corRepository = corRepository;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        categoria = categoriaRepository.save(new Categoria(null, "moda praia", new ArrayList<Produto>()));
        produto = produtoRepository.save(new Produto(null, "saída de praia", null, null, categoria, null, null));
        produtoId = produto.getId();
        cor = corRepository.save(new Cor(null, "rosa"));
    }

    private Integer criarVariacao(Produto produto, Cor cor, Double preco) {
        Variacao variacao = new Variacao();
        variacao.setProduto(produto);
        variacao.setCor(cor);
        variacao.setPreco(preco);

        return variacaoRepository.save(variacao).getId();
    }

    @Test
    void uploadFotoProduto_produtoExistente_retorna200ComUrl() throws Exception {
        when(cloudinaryService.upload(any())).thenReturn("https://fake-url.com/foto.jpg");

        mockMvc.perform(multipart("/api/produtos/{id}/foto", produtoId)
                .file("arquivo", "conteudo".getBytes()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.foto").value("https://fake-url.com/foto.jpg"));
    }

    @Test
    void uploadFotoProduto_produtoNaoEncontrado_retorna404() throws Exception {
        when(cloudinaryService.upload(any())).thenReturn("https://fake-url.com/foto.jpg");

        mockMvc.perform(multipart("/api/produtos/{id}/foto", 999)
                .file("arquivo", "conteudo".getBytes()))
                .andExpect(status().isNotFound());
    }

    @Test
    void uploadFotoVariacao_variacaoExistente_retorna200ComUrl() throws Exception {
        Integer id = criarVariacao(produto,cor, 55.4);
        when(cloudinaryService.upload(any())).thenReturn("https://fake-url.com/foto.jpg");

        mockMvc.perform(multipart("/api/variacoes/{id}/foto", id)
                        .file("arquivo", "conteudo".getBytes()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.foto").value("https://fake-url.com/foto.jpg"));
    }

    @Test
    void uploadFotoVariacao_variacaoNaoEncontrada_retorna404() throws Exception {
        when(cloudinaryService.upload(any())).thenReturn("https://fake-url.com/foto.jpg");

        mockMvc.perform(multipart("/api/variacoes/{id}/foto", 999)
                        .file("arquivo", "conteudo".getBytes()))
                .andExpect(status().isNotFound());
    }
}
