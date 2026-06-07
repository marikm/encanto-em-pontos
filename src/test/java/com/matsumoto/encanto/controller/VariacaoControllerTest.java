package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.domain.Categoria;
import com.matsumoto.encanto.domain.Cor;
import com.matsumoto.encanto.domain.Produto;
import com.matsumoto.encanto.domain.Variacao;
import com.matsumoto.encanto.dto.VariacaoRequest;
import com.matsumoto.encanto.repository.CategoriaRepository;
import com.matsumoto.encanto.repository.CorRepository;
import com.matsumoto.encanto.repository.ProdutoRepository;
import com.matsumoto.encanto.repository.VariacaoRepository;
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

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class VariacaoControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final VariacaoRepository variacaoRepository;
    private final CorRepository corRepository;
    private Integer produtoId;
    private Integer corId;


    @Autowired
    VariacaoControllerTest(WebApplicationContext context, ProdutoRepository produtoRepository,
                           CategoriaRepository categoriaRepository,
                           VariacaoRepository variacaoRepository, CorRepository corRepository ) {
        this.context = context;
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.variacaoRepository = variacaoRepository;
        this.corRepository = corRepository;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        //
        Categoria categoria = new Categoria(null, "moda praia", null);
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        //
        Produto produto = new Produto(null, "biquini rendado", "lindo", null, categoriaSalva, null, null);
        Produto produtoSalvo = produtoRepository.save(produto);
        this.produtoId = produtoSalvo.getId();
        //
        Cor cor = new Cor(null, "azul");
        Cor corSalva = corRepository.save(cor);
        this.corId = corSalva.getId();
    }


    @Test
    void criar_comDadosValidos_retorna201() throws Exception {
        VariacaoRequest variacaoRequest = new VariacaoRequest(corId, "P", null, 150.50, 5, null);
        String json = objectMapper.writeValueAsString(variacaoRequest);

        mockMvc.perform(post("/api/produtos/" + produtoId +"/variacoes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cor").value("azul"))
                .andExpect(jsonPath("$.tamanho").value("P"))
                .andExpect(jsonPath("$.preco").value(150.50))
                .andExpect(jsonPath("$.nomeProduto").value("biquini rendado"));
    }

    @Test
    void criar_produtoNaoEncontrado_retorna404() throws Exception {
        VariacaoRequest variacaoRequest = new VariacaoRequest(corId, "P", null, 150.50, 5, null);
        String json = objectMapper.writeValueAsString(variacaoRequest);

        mockMvc.perform(post("/api/produtos/999/variacoes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void criar_semPreco_retorna400() throws Exception {
        VariacaoRequest variacaoRequest = new VariacaoRequest(corId, "P", null, null, 5, null);
        String json = objectMapper.writeValueAsString(variacaoRequest);

        mockMvc.perform(post("/api/produtos/" +produtoId+ "/variacoes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizar_variacaoExistente_retorna200() throws Exception {
        Cor cor = corRepository.findById(corId).get();
        Produto produto = produtoRepository.findById(produtoId).get();
        Variacao variacao = new Variacao(null, cor, "P", null, 150.40, 5, null, produto);
        Variacao variacaoSalva = variacaoRepository.save(variacao);
        Integer variacaoId = variacaoSalva.getId();

        VariacaoRequest request = new VariacaoRequest(corId, "P", null, 149.50, 5, null);
        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/api/variacoes/" + variacaoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preco").value(149.50));

    }

    @Test
    void atualizar_variacaoNaoEncontrada_retorna404() throws Exception {
        VariacaoRequest request = new VariacaoRequest(corId, "P", null, 149.50, 5, null);
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/variacoes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_variacaoExistente_retorna204() throws Exception {
        Cor cor = corRepository.findById(corId).get();
        Produto produto = produtoRepository.findById(produtoId).get();
        Variacao variacao = new Variacao(null, cor, "P", null, 150.40, 5, null, produto);
        Variacao variacaoSalva = variacaoRepository.save(variacao);
        Integer variacaoId = variacaoSalva.getId();

        mockMvc.perform(delete("/api/variacoes/" + variacaoId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletar_variacaoNaoEncontrada_retorna404() throws Exception {
        mockMvc.perform(delete("/api/variacoes/999")).andExpect(status().isNotFound());
    }
}



