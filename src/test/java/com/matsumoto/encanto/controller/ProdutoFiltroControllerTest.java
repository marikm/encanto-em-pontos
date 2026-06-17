package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.domain.Categoria;
import com.matsumoto.encanto.domain.Cor;
import com.matsumoto.encanto.domain.Produto;
import com.matsumoto.encanto.domain.Variacao;
import com.matsumoto.encanto.repository.CategoriaRepository;
import com.matsumoto.encanto.repository.CorRepository;
import com.matsumoto.encanto.repository.ProdutoRepository;
import com.matsumoto.encanto.repository.VariacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProdutoFiltroControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext context;
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CorRepository corRepository;
    private final VariacaoRepository variacaoRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Categoria categoriaSalva;
    private Categoria categoriaSalva2;
    private Produto produtoSalvo;
    private Produto produtoSalvo2;

    @Autowired
    public ProdutoFiltroControllerTest(WebApplicationContext context, ProdutoRepository produtoRepository,
                                       CategoriaRepository categoriaRepository, CorRepository corRepository,
                                       VariacaoRepository variacaoRepository) {
        this.context = context;
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.corRepository = corRepository;
        this.variacaoRepository = variacaoRepository;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        categoriaSalva = categoriaRepository.save(new Categoria(null, "Biquíni", new ArrayList<>()));
        categoriaSalva2 = categoriaRepository.save(new Categoria(null, "Tapete", new ArrayList<>()));
        produtoSalvo = produtoRepository.save(new Produto(null, "biquini rendado", "lindo", null, categoriaSalva, null, null));
        produtoSalvo2 = produtoRepository.save(new Produto(null, "tapete", "jogo de tapetes", null, categoriaSalva2, null, null));
    }

    private void criarVariacao(Produto produto, Cor cor, Double preco) {
        Variacao variacao = new Variacao();
        variacao.setProduto(produto);
        variacao.setCor(cor);
        variacao.setPreco(preco);
        variacaoRepository.save(variacao);
    }

    @Test
    void listar_semFiltros_retornaTodosOsProdutos() throws Exception {
        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("biquini rendado"));
    }

    @Test
    void listar_comFiltroCategoria_retornaApenasOsProdutosDaCategoria() throws Exception {
        mockMvc.perform(get("/api/produtos").param("categoriaId", categoriaSalva.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void listar_comFiltroCor_retornaApenasOsProdutosComVariacaoNaCor() throws Exception {
        Cor corSalva = corRepository.save(new Cor(null, "azul"));
        criarVariacao(produtoSalvo, corSalva, 50.0);

        mockMvc.perform(get("/api/produtos").param("cor", corSalva.getNome()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void listar_comFiltroFaixaDePreco_retornaApenasOsProdutosDentroDoIntervalo() throws Exception {
        Cor corSalva = corRepository.save(new Cor(null, "azul"));
        criarVariacao(produtoSalvo, corSalva, 50.0);
        criarVariacao(produtoSalvo2, corSalva, 70.0);

        mockMvc.perform(get("/api/produtos").param("precoMin", "30.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));

        mockMvc.perform(get("/api/produtos").param("precoMax", "60.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void listar_comFiltroBusca_retornaApenasOsProdutosComNomeCorrespondente() throws Exception {
        mockMvc.perform(get("/api/produtos").param("busca", "rendado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("biquini rendado"))
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void listar_semCorrespondencia_retornaListaVazia() throws Exception {
        mockMvc.perform(get("/api/produtos").param("cor", "rosa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void listar_comCategoriaIdInvalido_retornaBadRequest() throws Exception {
        mockMvc.perform(get("/api/produtos").param("categoriaId", "abc"))
                .andExpect(status().isBadRequest());
    }
}
