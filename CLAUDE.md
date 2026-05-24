# Encanto — API REST

E-commerce de peças de crochê artesanal sob encomenda.

## Stack

- Java 21 · Spring Boot 3 · Spring Data JPA
- PostgreSQL (produção) · H2 (perfil `test`)
- Cloudinary (upload de imagens — F05)
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
| `Categoria` | ✅ | ✅ | ❌ | ⚠️ stub hardcoded | — | ✅ `CategoriaNaoEncontradaException` |
| `Material` | ✅ | ✅ | ❌ | ❌ | — | ❌ |
| `Produto` | ✅ | ✅ | ✅ | ✅ POST · PUT · DELETE /api/produtos | ✅ Request/Response | ✅ `ProdutoNaoEncontradoException` |
| `Variacao` | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ |

### GlobalExceptionHandler
Registrado: `CorNaoEncontradaException` · `ProdutoNaoEncontradoException` · `CategoriaNaoEncontradaException`

### ProdutoService — métodos implementados
- `criar` ✅ — busca Categoria + Materiais, monta Produto com setters, save retorna entidade com ID
- `listarTodos` ✅ — findAll + stream/map para Response
- `buscarPorId` ✅ — orElseThrow com ProdutoNaoEncontradoException
- `atualizar` ✅ — busca produto existente, atualiza campos, save retorna entidade com ID
- `deletar` ✅ — busca antes de deletar para garantir que existe

### ProdutoController — endpoints implementados
- `POST /api/produtos` → criar · retorna 201 Created
- `PUT /api/produtos/{id}` → atualizar · retorna 200 OK
- `DELETE /api/produtos/{id}` → deletar · retorna 204 No Content

---

## Ordem das features

### FASE 1 — Catálogo
| # | Feature | Status |
|---|---------|--------|
| F01 | Produto completo | ✅ concluída |
| F02 | Variação do produto | ❌ |
| F03 | Catálogo público (GET paginado) | ❌ |
| F04 | Filtro de produtos (Specification) | ❌ |
| F05 | Upload de imagens — Cloudinary | ❌ |

### FASE 2 — Autenticação (iniciar após F05)
| # | Feature | Status |
|---|---------|--------|
| F06 | Entidade Pessoa + Endereço | ❌ |
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

## Próximo passo

**F02 · Variação do produto** — arquivos a criar:
- `Variacao` (entity)
- `VariacaoRepository`
- `VariacaoService`
- `VariacaoController`
- `VariacaoRequest` / `VariacaoResponse`
- `VariacaoNaoEncontradaException`

Endpoints:
```
POST   /api/produtos/{id}/variacoes  → adicionar variação (ADMIN)
PUT    /api/variacoes/{id}           → editar (ADMIN)
DELETE /api/variacoes/{id}           → remover (ADMIN)
```
