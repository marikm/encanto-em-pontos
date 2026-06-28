package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.domain.Categoria;
import com.matsumoto.encanto.dto.CategoriaRequest;
import com.matsumoto.encanto.repository.CategoriaRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CategoriaControllerTest {

    private MockMvc mockMvc;
    private final WebApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CategoriaRepository categoriaRepository;

    @Autowired
    CategoriaControllerTest(WebApplicationContext context, CategoriaRepository categoriaRepository) {
        this.context = context;
        this.categoriaRepository = categoriaRepository;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    }

    @Test
    void listarCategorias_comCategoriasCadastradas_retorna200ComLista() throws Exception {
        categoriaRepository.save(new Categoria(null, "Moda Praia", null));
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Moda Praia"));
    }

    @Test
    void listarCategorias_bancovazio_retorna200ComListaVazia() throws Exception {
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void buscarPorId_comIdValido_retorna200ComNomeCorreto() throws Exception {
        Integer categoriaId =  categoriaRepository.save(new Categoria(null, "Moda Praia", null)).getId();
        mockMvc.perform(get("/api/categorias/{id}", categoriaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Moda Praia"));

    }

    @Test
    void buscarPorId_comIdInexistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/categorias/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void criar_comNomeValido_retorna201ComIdENome() throws Exception {
        CategoriaRequest categoriaRequest = new CategoriaRequest("Decoração");
        String json = objectMapper.writeValueAsString(categoriaRequest);

        mockMvc.perform(post("/api/categorias").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Decoração"));

    }

    @Test
    void criar_semCampoNome_retorna400() throws Exception {
        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

}
