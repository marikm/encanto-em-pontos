package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.domain.Endereco;
import com.matsumoto.encanto.domain.Pessoa;
import com.matsumoto.encanto.domain.enums.PerfilAcesso;
import com.matsumoto.encanto.dto.EnderecoRequest;
import com.matsumoto.encanto.repository.EnderecoRepository;
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

import java.time.LocalDate;
import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EnderecoControllerTest {

    private MockMvc mockMvc;
    private final WebApplicationContext context;
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Pessoa pessoa;
    private Integer pessoaId;

    @Autowired
    public EnderecoControllerTest(WebApplicationContext context,
                                  PessoaRepository pessoaRepository,
                                  EnderecoRepository enderecoRepository) {
        this.context = context;
        this.pessoaRepository = pessoaRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

       this.pessoa = createPessoa();

        this.pessoaId = pessoaRepository.save(pessoa).getId();
    }
    @Test
    void adicionarEndereco_dadosValidos_retorna201() throws Exception {
        EnderecoRequest enderecoRequest = new EnderecoRequest("Rua dos Limoeiros", "270", null, "Planalto",
                "Barbacena", "São Paulo", "36200-006");
        String json = objectMapper.writeValueAsString(enderecoRequest);

        mockMvc.perform(post("/api/clientes/{pessoaId}/enderecos", this.pessoaId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.logradouro").value("Rua dos Limoeiros"))
                .andExpect(jsonPath("$.numero").value("270"))
                .andExpect(jsonPath("$.bairro").value("Planalto"))
                .andExpect(jsonPath("$.cidade").value("Barbacena"))
                .andExpect(jsonPath("$.pessoaNome").value("Maria Aparecida da Silva"));
    }

    @Test
    void adicionarEndereco_pessoaInexistente_retorna404() throws Exception {
        EnderecoRequest enderecoRequest = new EnderecoRequest("Rua dos Limoeiros", "270", null, "Planalto",
                "Barbacena", "São Paulo", "36200-006");
        String json = objectMapper.writeValueAsString(enderecoRequest);

        mockMvc.perform(post("/api/clientes/{pessoaId}/enderecos", 999).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void adicionarEndereco_campoObrigatorioAusente_retorna400() throws Exception {
        mockMvc.perform(post("/api/clientes/{pessoaId}/enderecos", this.pessoaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarEnderecos_pessoaComEnderecos_retorna200() throws Exception {
        Endereco endereco = createEndereco();
        enderecoRepository.save(endereco);
        mockMvc.perform(get("/api/clientes/{pessoaId}/enderecos", this.pessoaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].logradouro").value("Rua dos Limoeiros"));
    }

    @Test
    void removerEndereco_enderecoExistente_retorna204() throws Exception {
        Endereco endereco = createEndereco();
        Integer enderecoId = enderecoRepository.save(endereco).getId();

        mockMvc.perform(delete("/api/clientes/{pessoaId}/enderecos/{enderecoId}", this.pessoaId, enderecoId))
                .andExpect(status().isNoContent());
    }

    @Test
    void removerEndereco_enderecoInexistente_retorna404() throws Exception {
        mockMvc.perform(delete("/api/clientes/{pessoaId}/enderecos/{enderecoId}", this.pessoaId, 999))
                .andExpect(status().isNotFound());
    }

    private Pessoa createPessoa () {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Maria Aparecida da Silva");
        pessoa.setCpf("12345678908");
        pessoa.setEmail("maria@gmail.com");
        pessoa.setSenha("123456");
        pessoa.setDataNascimento(Date.valueOf(LocalDate.of(1998, 7, 3)));
        pessoa.setPerfilAcesso(PerfilAcesso.CLIENTE);

        return pessoa;
    }

    private Endereco createEndereco() {
        return new Endereco(null, "Rua dos Limoeiros", "270", null, "Planalto", "Barbacena", "São Paulo", "36200-006", this.pessoa);
    }
}
