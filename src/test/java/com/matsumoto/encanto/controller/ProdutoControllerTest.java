package com.matsumoto.encanto.controller;



import com.matsumoto.encanto.domain.Categoria;
import com.matsumoto.encanto.domain.Produto;
import com.matsumoto.encanto.dto.ProdutoRequest;
import com.matsumoto.encanto.repository.CategoriaRepository;
import com.matsumoto.encanto.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.ArrayList;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProdutoControllerTest {

    private MockMvc mockMvc;
    private final WebApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private Integer categoriaId;

    @Autowired
    ProdutoControllerTest(WebApplicationContext context, CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.context = context;
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        Categoria categoria = new Categoria(null, "Biquíni", new ArrayList<>());
        categoriaId = categoriaRepository.save(categoria).getId();
    }

    @Test
    void criar_comDadosValidos_retorna201() throws Exception {
        // 1. Prepara o pedido (monta o JSON)
        ProdutoRequest request = new ProdutoRequest("Biquíni de crochê", "Lindo para o verão", null, categoriaId, null);
        String json = objectMapper.writeValueAsString(request);

        // 2. Bate na porta (faz o POST)
        mockMvc.perform(post("/api/produtos").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated())
                // 3. Confere status e corpo
                .andExpect(jsonPath("$.nome").value("Biquíni de crochê"));


    }
    @Test
    void criar_semNome_retorna400() throws Exception {
        String[] nomesInvalidos = {null, "", " "};

        for (String nome : nomesInvalidos) {
            ProdutoRequest request = new ProdutoRequest(nome, "roupa de praia", null, categoriaId, null);
            String json = objectMapper.writeValueAsString(request);
            mockMvc.perform(post("/api/produtos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void
    atualizar_produtoExistente_retorna200() throws Exception {
        Categoria categoria = categoriaRepository.findById(categoriaId).get();
        Produto produto = new Produto(null, "saída de praia", null, null, categoria, null, null);
        Produto produtoSalvo = produtoRepository.save(produto);
        Integer produtoId = produtoSalvo.getId();

        ProdutoRequest produtoRequest =  new ProdutoRequest("biquíni rendado", "roupa de praia", null, categoriaId, null);
        String json = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(put("/api/produtos/" + produtoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("biquíni rendado"));

    }

    @Test
    void atualizar_produtoNaoEncontrado_retorna404() throws Exception {
        ProdutoRequest request = new ProdutoRequest("saia longa", "perfeito para festas", null, categoriaId, null);
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/produtos/999").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void
    deletar_produtoExistente_retorna204() throws Exception {
        Categoria categoria = categoriaRepository.findById(categoriaId).get();
        Produto produto = new Produto(null, "saída de praia", null, null, categoria, null, null);
        Produto produtoSalvo = produtoRepository.save(produto);
        Integer produtoId = produtoSalvo.getId();

        mockMvc.perform(delete("/api/produtos/" + produtoId)).andExpect(status().isNoContent());
    }

    @Test
    void
    deletar_produtoNaoEncontrado_retorna404() throws Exception {
        mockMvc.perform(delete("/api/produtos/999")).andExpect(status().isNotFound());

    }

    @Test
    void listarProdutos_comProdutosCadastrados_retorna200ComPagina() throws Exception {
        Categoria categoria = categoriaRepository.findById(categoriaId).get();
        Produto produto = new Produto(null, "biquíni rendado", null, null, categoria, null, null);
        Produto produto2 = new Produto(null, "saída de praia", null, null, categoria, null, null);
        produtoRepository.save(produto);
        produtoRepository.save(produto2);

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("biquíni rendado"))
                .andExpect(jsonPath("$.content[1].nome").value("saída de praia"));
   }

    @Test
    void buscarPorId_comIdValido_retorna200ComCamposEProduto() throws Exception {
        Categoria categoria = categoriaRepository.findById(categoriaId).get();
        Produto produto = new Produto(null, "biquíni rendado", null, null, categoria, null, null);
        Produto produtoSalvo = produtoRepository.save(produto);
        Integer produtoId = produtoSalvo.getId();


        mockMvc.perform(get("/api/produtos/" + produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("biquíni rendado"))
                .andExpect(jsonPath("$.categoriaNome").value("Biquíni"))
                .andExpect(jsonPath("$.variacoes").isArray());
    }

    @Test
    void buscarPorId_comIdInvalido_retorna404() throws Exception {
        mockMvc.perform(get("/api/produtos/999")).andExpect(status().isNotFound());
    }




}
