package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.domain.Pessoa;
import com.matsumoto.encanto.domain.enums.PerfilAcesso;
import com.matsumoto.encanto.dto.PessoaRequest;
import com.matsumoto.encanto.repository.PessoaRepository;
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

import java.sql.Date;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PessoaControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext context;
    private final PessoaRepository pessoaRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public PessoaControllerTest(WebApplicationContext context,
                                PessoaRepository pessoaRepository) {
        this.context = context;
        this.pessoaRepository = pessoaRepository;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void cadastrarPessoa_dadosValidos_retorna201() throws Exception {
        PessoaRequest pessoaRequest = createPessoaRequest();
        String json = objectMapper.writeValueAsString(pessoaRequest);

        mockMvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria"))
                .andExpect(jsonPath("$.cpf").value("13454612312"))
                .andExpect(jsonPath("$.email").value("maria@gmail.com"))
                .andExpect(jsonPath("$.perfil").value("CLIENTE"));
    }

    @Test
    void cadastrarPessoa_cpfDuplicado_retorna409() throws Exception {
        Pessoa pessoa = createPessoa();
        pessoaRepository.save(pessoa);

        PessoaRequest pessoaRequest = createPessoaRequest();
        String json = objectMapper.writeValueAsString(pessoaRequest);

        mockMvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void cadastrarPessoa_campoObrigatorioAusente_retorna400() throws Exception {
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")).andExpect(status().isBadRequest());
    }

    @Test
    void buscarPessoaPorId_idExistente_retorna200() throws Exception {
        Pessoa pessoa = createPessoa();
        Integer pessoaId = pessoaRepository.save(pessoa).getId();

        mockMvc.perform(get("/api/clientes/{id}", pessoaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria"))
                .andExpect(jsonPath("$.cpf").value("13454612312"))
                .andExpect(jsonPath("$.email").value("maria@gmail.com"))
                .andExpect(jsonPath("$.perfil").value("CLIENTE"));
    }

    @Test
    void buscarPessoaPorId_idInexistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/clientes/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarPessoa_dadosValidos_retorna200() throws Exception {
        Pessoa pessoa = createPessoa();
        Integer pessoaId = pessoaRepository.save(pessoa).getId();

        PessoaRequest pessoaRequest = new PessoaRequest();
        pessoaRequest.setNome("João Paulo");
        pessoaRequest.setCpf("13454612312");
        pessoaRequest.setEmail("maria@gmail.com");
        pessoaRequest.setDataNascimento(Date.valueOf(LocalDate.of(1998, 7, 3)));
        pessoaRequest.setSenha("123456");
        pessoaRequest.setPerfil(PerfilAcesso.CLIENTE);

        String json = objectMapper.writeValueAsString(pessoaRequest);

        mockMvc.perform(put("/api/clientes/{id}", pessoaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Paulo"));
    }

    private Pessoa createPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Maria");
        pessoa.setCpf("13454612312");
        pessoa.setEmail("maria@gmail.com");
        pessoa.setDataNascimento(Date.valueOf(LocalDate.of(1998, 7, 3)));
        pessoa.setSenha("123456");
        pessoa.setPerfilAcesso(PerfilAcesso.CLIENTE);

        return pessoa;
    }

    private PessoaRequest createPessoaRequest() {
        PessoaRequest pessoaRequest = new PessoaRequest();
        pessoaRequest.setNome("Maria");
        pessoaRequest.setCpf("13454612312");
        pessoaRequest.setEmail("maria@gmail.com");
        pessoaRequest.setDataNascimento(Date.valueOf(LocalDate.of(1998, 7, 3)));
        pessoaRequest.setSenha("123456");
        pessoaRequest.setPerfil(PerfilAcesso.CLIENTE);

        return pessoaRequest;
    }
}
