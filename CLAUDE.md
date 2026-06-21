# Encanto — API REST

E-commerce de peças de crochê artesanal sob encomenda.

## Stack

- Java 21 · Spring Boot 4 · Spring Data JPA
- PostgreSQL (produção) · H2 (perfil `test`)
- Cloudinary (upload de imagens — F05)
- Spring Security · BCrypt (autenticação — F06+)
- Lombok · Bean Validation · GlobalExceptionHandler

**Package base:** `com.matsumoto.encanto`  
**Branches:** `main` (produção) · `develop` (integração) · `feature/*` (desenvolvimento ativo)

---

## Padrões obrigatórios

- Arquitetura em camadas: Controller → Service → Repository
- DTOs separados da entidade: `NomeEntidadeRequest` (entrada) e `NomeEntidadeResponse` (saída)
- Exception customizada por entidade + registrar no `GlobalExceptionHandler`
- Injeção de dependência via construtor (nunca `@Autowired` em campo)
- Bean Validation nas entidades e requests: `@NotNull`, `@NotBlank`, `@Positive`
- Controllers sempre retornam `ResponseEntity` com status HTTP correto
- Lombok: `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)`
- Código em inglês · comentários explicativos em português

### Padrão de branches (inglês)
```
feature/f02-variacao-produto      nova feature (prefixo com código da feature)
bugfix/fix-categoria-controller   correção de bug
refactor/categoria-add-service    refatoração
```
Fluxo obrigatório: criar branch a partir de `develop` → commitar → abrir PR para `develop` → merge → PR de `develop` para `main` quando estável.
Nunca commitar diretamente em `develop` ou `main`.

### Padrão de commits (inglês)
```
feat:     add [file/feature]
fix:      fix [problem]
refactor: refactor [what and why]
test:     add tests for [class]
chore:    configure [dependency/environment]
```

---

## O que já existe no código

| Recurso | Entity | Repository | Service | Controller | DTOs | Exception |
|---------|--------|------------|---------|------------|------|-----------|
| `Cor` | ✅ | ✅ | ✅ | ✅ GET /api/cores | — | ✅ `CorNaoEncontradaException` |
| `Categoria` | ✅ | ✅ | ❌ | ⚠️ stub removido, controller vazio | — | ✅ `CategoriaNaoEncontradaException` |
| `Material` | ✅ | ✅ | ❌ | ❌ | — | ❌ |
| `Produto` | ✅ | ✅ | ✅ | ✅ GET (paginado) · GET /{id} · POST · PUT · DELETE /api/produtos | ✅ Request/Response/DetalheResponse | ✅ `ProdutoNaoEncontradoException` |
| `Variacao` | ✅ | ✅ | ✅ | ✅ POST · PUT · DELETE /api/variacoes | ✅ Request/Response | ✅ `VariacaoNaoEncontradaException` |
| `Pessoa` | ✅ | ✅ | ✅ | ✅ POST · GET /{id} · PUT /api/clientes | ✅ Request/Response | ✅ `PessoaNaoEncontradaException` · `CpfJaCadastradoException` |
| `Endereco` | ✅ | ✅ | ✅ | 🔄 em andamento /api/clientes/{id}/enderecos | ✅ Request/Response | ✅ `EnderecoNaoEncontradoException` |

### GlobalExceptionHandler
Registrado: `CorNaoEncontradaException` · `ProdutoNaoEncontradoException` · `CategoriaNaoEncontradaException` · `VariacaoNaoEncontradaException` · `PessoaNaoEncontradaException` · `EnderecoNaoEncontradoException` · `CpfJaCadastradoException` (409 Conflict)

### ProdutoService — métodos implementados
- `criar` ✅ — busca Categoria + Materiais, monta Produto com setters, save retorna entidade com ID
- `listarTodos(Pageable)` ✅ — findAll(pageable) + Page.map para ProdutoResponse
- `buscarPorId` ✅ — orElseThrow com ProdutoNaoEncontradoException · retorna ProdutoDetalheResponse
- `atualizar` ✅ — busca produto existente, atualiza campos, save retorna entidade com ID
- `deletar` ✅ — busca antes de deletar para garantir que existe
- `toDetalheResponse` ✅ (private) — mapeia Produto → ProdutoDetalheResponse com lista de VariacaoResponse · null-safe para materiais e variacoes
- `filtrar` ✅ — combina Specifications com `.and()` e chama `findAll(spec, pageable)`
- `atualizarFoto` ✅ — busca produto, seta URL da foto, salva e retorna ProdutoResponse

### ProdutoSpecification — filtros implementados (F04)
- `porCategoria(Integer categoriaId)` ✅ — join categoria · cb.equal por id
- `porCor(String cor)` ✅ — join variacoes → join cor · cb.equal por nome
- `precoMinimo(Double precoMin)` ✅ — join variacoes · cb.greaterThanOrEqualTo
- `precoMaximo(Double precoMax)` ✅ — join variacoes · cb.lessThanOrEqualTo
- `porBusca(String busca)` ✅ — cb.like no nome do produto

### ProdutoRepository — interfaces estendidas
`JpaRepository<Produto, Integer>` · `JpaSpecificationExecutor<Produto>`

### ProdutoDetalheResponse — campos
`id` · `nome` · `descricao` · `foto` · `categoriaNome` · `materiaisNomes` (List<String>) · `variacoes` (List<VariacaoResponse>)

### ProdutoController — endpoints implementados
- `GET /api/produtos` → listagem paginada · retorna 200 OK · público
- `GET /api/produtos/{id}` → detalhe com variações · retorna 200 OK · público
- `POST /api/produtos` → criar · retorna 201 Created
- `PUT /api/produtos/{id}` → atualizar · retorna 200 OK
- `DELETE /api/produtos/{id}` → deletar · retorna 204 No Content

### VariacaoService — métodos implementados
- `criar` ✅ — busca Produto + Cor, monta Variacao com setters, save retorna entidade com ID
- `atualizar` ✅ — busca variação existente, busca Cor por corId, atualiza campos
- `deletar` ✅ — busca antes de deletar para garantir que existe
- `atualizarFoto` ✅ — busca variação, seta URL da foto, salva e retorna VariacaoResponse

### VariacaoController — endpoints implementados
- `POST /api/produtos/{id}/variacoes` → criar · retorna 201 Created
- `PUT /api/variacoes/{id}` → atualizar · retorna 200 OK
- `DELETE /api/variacoes/{id}` → deletar · retorna 204 No Content

### SecurityConfig (F06)
- `passwordEncoder()` ✅ — `@Bean` de `BCryptPasswordEncoder`
- `filterChain()` ✅ — desabilita CSRF e libera todos os endpoints temporariamente até o F07 configurar o JWT

### PessoaService — métodos implementados (F06)
- `cadastrar` ✅ — verifica CPF único via `existsByCpf`, monta `Pessoa`, hasheia senha com `BCryptPasswordEncoder`, salva
- `buscarPorId` ✅ — `findById` + `orElseThrow` com `PessoaNaoEncontradaException`
- `atualizar` ✅ — busca existente, atualiza campos incluindo re-hash da senha, salva
- `toResponse` ✅ (private) — mapeia `Pessoa` → `PessoaResponse` sem expor o campo `senha`

### EnderecoService — métodos implementados (F06)
- `adicionar` ✅ — busca `Pessoa` por id, monta `Endereco`, seta relacionamento, salva
- `listarPorPessoa` ✅ — `findByPessoaId` + stream map para `EnderecoResponse`
- `remover` ✅ — busca endereço ou lança `EnderecoNaoEncontradoException`, deleta
- `toResponse` ✅ (private) — mapeia `Endereco` → `EnderecoResponse` com `pessoaNome`

### PessoaController — endpoints implementados (F06)
- `POST /api/clientes` → cadastrar · retorna 201 Created
- `GET /api/clientes/{id}` → buscar por id · retorna 200 OK
- `PUT /api/clientes/{id}` → atualizar · retorna 200 OK

### EnderecoController — endpoints implementados (F06)
- `POST /api/clientes/{pessoaId}/enderecos` → adicionar endereço · retorna 201 Created
- `GET /api/clientes/{pessoaId}/enderecos` → listar endereços da pessoa · retorna 200 OK
- `DELETE /api/clientes/{pessoaId}/enderecos/{enderecoId}` → remover · retorna 204 No Content

### Pessoa — observações de domínio (F06)
- `perfilAcesso` é enum `PerfilAcesso` (`CLIENTE`/`ADMIN`) persistido com `@Enumerated(EnumType.STRING)`
- `cpf` e `email` têm `@Column(unique = true)` — unicidade garantida no banco
- `senha` nunca é exposta no `PessoaResponse` — hash aplicado no Service com BCrypt
- `CpfJaCadastradoException` lança 409 Conflict (não 404) quando CPF já existe

### ImagemController — endpoints implementados (F05)
- `POST /api/produtos/{id}/foto` → upload foto do produto · retorna 200 OK com ProdutoResponse
- `POST /api/variacoes/{id}/foto` → upload foto da variação · retorna 200 OK com VariacaoResponse

### CloudinaryService
- `upload(MultipartFile)` ✅ — envia arquivo ao Cloudinary, aplica transformação eager 600×600, retorna URL pública

### Variacao — observações de domínio
- `cor` é FK para a entidade `Cor` (`@ManyToOne`) — não texto livre
- `VariacaoRequest` recebe `corId` (Integer) com `@NotNull`
- `VariacaoResponse` expõe `cor` (String) com o nome da cor

---

## Ordem das features

### FASE 1 — Catálogo
| # | Feature | Status |
|---|---------|--------|
| F01 | Produto completo | ✅ concluída |
| F02 | Variação do produto | ✅ concluída |
| F03 | Catálogo público (GET paginado) | ✅ concluída |
| F04 | Filtro de produtos (Specification) | ✅ concluída |
| F05 | Upload de imagens — Cloudinary | ✅ concluída |

### FASE 2 — Autenticação (iniciar após F05)
| # | Feature | Status |
|---|---------|--------|
| F06 | Entidade Pessoa + Endereço | 🔄 implementação concluída · testes pendentes |
| F07 | Login com JWT | ❌ |

### FASE 3 — Pedido (iniciar após F07)
| # | Feature | Status |
|---|---------|--------|
| F08 | Pedido + ItemPedido (carrinho) | ❌ |
| F09 | Confirmação do pedido | ❌ |
| F10 | Cálculo de frete (ViaCEP) | ❌ |

### FASE 4 — Pagamento (iniciar após F10)
| # | Feature | Status |
|---|---------|--------|
| F11 | Integração Mercado Pago | ❌ |
| F12 | Webhook de confirmação | ❌ |

### FASE 5 — Gestão (iniciar após F12)
| # | Feature | Status |
|---|---------|--------|
| F13 | Meus pedidos — área do cliente | ❌ |
| F14 | Gerenciar pedidos — painel admin | ❌ |
| F15 | Gerenciar clientes — painel admin | ❌ |

---

## Padrão de testes de integração (Spring Boot 4)

### Tipo de teste utilizado
`@SpringBootTest` + `MockMvc` manual + H2 em memória — testa a requisição HTTP de ponta a ponta (Controller → Service → Repository → banco).

### Anotações obrigatórias na classe de teste
```java
@SpringBootTest       // sobe o contexto Spring completo
@ActiveProfiles("test") // usa application-test.properties com H2
@Transactional        // reverte o banco após cada teste (isolamento)
```

### Como montar o MockMvc (Spring Boot 4)
**Não usar `@AutoConfigureMockMvc`** — o pacote mudou e ainda é instável no Spring Boot 4.
Montar manualmente no `@BeforeEach`:
```java
@BeforeEach
void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
}
```
Import: `org.springframework.test.web.servlet.setup.MockMvcBuilders`

### Injeção de dependências no construtor do teste
Usar `@Autowired` no construtor (necessário para o JUnit 5 + SpringExtension resolver os parâmetros).
**Não é field injection** — é construtor, o que está dentro do padrão do projeto.
```java
@Autowired
NomeDaClasseTest(WebApplicationContext context, NomeRepository repository) { ... }
```

### ObjectMapper (Jackson 3 — Spring Boot 4)
Pacote mudou de `com.fasterxml.jackson` para `tools.jackson`.
Instanciar diretamente no campo — não injetar pelo construtor:
```java
private final ObjectMapper objectMapper = new ObjectMapper();
```
Import: `tools.jackson.databind.ObjectMapper`

### Rodar os testes
O runner do IntelliJ pode ser incompatível com o JUnit Platform do Spring Boot 4.
Sempre rodar pelo Maven:
```
mvn test -Dtest=NomeDaClasseTest
```

### Estrutura de um método de teste
```java
@Test
void acao_condicao_resultadoEsperado() throws Exception {
    // 1. monta o JSON de entrada
    String json = objectMapper.writeValueAsString(new MinhaRequest(...));

    // 2. executa a requisição
    mockMvc.perform(post("/api/recurso")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
    // 3. verifica status e corpo
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.campo").value("valor esperado"));
}
```

### Cenários obrigatórios por endpoint
- Caminho feliz: dados válidos → status correto + campo esperado na resposta
- Recurso não encontrado: ID inválido → 404
- Validação: campo obrigatório ausente → 400

### Mockando serviços externos nos testes (F05)
Quando o controller depende de um serviço externo (ex: Cloudinary), usar `@MockitoBean` no campo da classe de teste.
Import: `org.springframework.test.context.bean.override.mockito.MockitoBean`
Requer `mockito-core` no `pom.xml` com `scope test` (não vem junto nos starters modulares do Spring Boot 4).
Programar o comportamento com `when(mock.metodo(any())).thenReturn(valor)` antes da chamada ao MockMvc.

---

## Próximo passo

**Concluir F06** — implementar `EnderecoController` · rodar testes de integração · abrir PR para `develop`
**Depois: F07 · Login com JWT** — FASE 2 — Autenticação
Branch a criar: `feature/f07-login-jwt` a partir de `develop`
