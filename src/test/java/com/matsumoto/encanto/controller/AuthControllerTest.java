package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.domain.Pessoa;
import com.matsumoto.encanto.domain.enums.PerfilAcesso;
import com.matsumoto.encanto.dto.LoginRequest;
import com.matsumoto.encanto.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;
    private Integer idPessoa;

    @Autowired
    AuthControllerTest(WebApplicationContext context, PessoaRepository pessoaRepository,
                       PasswordEncoder passwordEncoder) {
        this.context = context;
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        // salva uma Pessoa com senha hasheada
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Maria");
        pessoa.setCpf("13454612312");
        pessoa.setEmail("maria@gmail.com");
        pessoa.setDataNascimento(Date.valueOf(LocalDate.of(1998, 7, 3)));
        pessoa.setSenha(passwordEncoder.encode("senha123"));
        pessoa.setPerfilAcesso(PerfilAcesso.CLIENTE);
        this.idPessoa = pessoaRepository.save(pessoa).getId();
    }

    @Test
    void login_comCredenciaisValidas_retorna200ComToken() throws Exception {
        String json = objectMapper.writeValueAsString(new LoginRequest("maria@gmail.com", "senha123"));

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_comSenhaErrada_retorna401() throws Exception {
        String json = objectMapper.writeValueAsString(new LoginRequest("maria@gmail.com", "senhaerrada"));

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isUnauthorized());
    }

    @Test
    void login_semCampos_retorna400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")).andExpect(status().isBadRequest());
    }

    @Test
    void login_comEmailInexistente_retorna401() throws Exception {
        String json = objectMapper.writeValueAsString(new LoginRequest("mari@gmail.com", "senha123"));

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isUnauthorized());
    }

    @Test
    void login_semEmail_retorna400() throws Exception {
        String json = objectMapper.writeValueAsString(new LoginRequest("", "senha123"));

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());
    }

    @Test
    void login_semSenha_retorna400() throws Exception {
        String json = objectMapper.writeValueAsString(new LoginRequest("maria@gmail.com", ""));

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());
    }

    @Test
    void listarProdutos_semToken_retorna200() throws Exception {
        mockMvc.perform(get("/api/produtos")).andExpect((status().isOk()));
    }

    @Test
    void buscarCliente_semToken_retorna401() throws Exception {
        mockMvc.perform(get("/api/clientes/{id}", this.idPessoa))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void buscarCliente_comTokenValido_retorna200() throws Exception {
        // passo 1: fazer login para obter o token
        String json = objectMapper.writeValueAsString(new LoginRequest("maria@gmail.com", "senha123"));

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andReturn();
        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token").asText();
        // passo 2: usar o token no header Authorization
        mockMvc.perform(get("/api/clientes/{id}", idPessoa)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void cadastrarCliente_semToken_retorna201() throws Exception {
        // POST /api/clientes é rota pública — não deve exigir Authorization
        String json = objectMapper.writeValueAsString(
                new com.matsumoto.encanto.dto.PessoaRequest(
                        "João Silva", "98765432100", "joao@gmail.com",
                        "senha456", Date.valueOf(LocalDate.of(1990, 1, 15)),
                        null, PerfilAcesso.CLIENTE));

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void buscarCliente_comTokenInvalido_retorna401() throws Exception {
        // o JwtAuthFilter deve rejeitar tokens forjados antes de chegar no controller
        mockMvc.perform(get("/api/clientes/{id}", this.idPessoa)
                        .header("Authorization", "Bearer ewogICJhbGciOiAiSFMyNTYiLAogICJ0eXAiOiAiSldUIgp9." +
                                "ewogICJzdWIiOiAiam9hb0BlbWFpbC5jb20iLAogICJub21lIjogIkpvYW8gU2lsdmEiLAogICJhZG1pbiI6IGZhbHNlCn0." +
                                "NDQ4NGI2MjY0NDg0YjYyNjQ0ODRiNjI2"))
                .andExpect(status().isUnauthorized());
    }
}
