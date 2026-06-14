# 🧶 Encanto em Pontos

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![License](https://img.shields.io/badge/licença-MIT-blue?style=for-the-badge)

---

## 📖 Descrição

**Encanto em Pontos** é um e-commerce B2C especializado em peças artesanais de crochê feitas sob encomenda. O cliente navega pelo catálogo, escolhe o produto, a cor e o tamanho desejados, e a artesã produz a peça conforme o pedido antes de realizar a entrega.

O sistema é composto por uma API REST no back-end (Java + Spring Boot) e uma interface web no front-end (React), desenvolvidos de forma independente.

---

## ✨ Funcionalidades principais

| Código | Funcionalidade | Perfil |
|--------|---------------|--------|
| RF01 | Login de usuário | Cliente / Admin |
| RF02 | Cadastro de cliente | Cliente |
| RF03 | Cadastro de produto | Admin |
| RF04 | Cadastro de categoria | Admin |
| RF05 | Cadastro de material | Admin |
| RF06 | Visualizar catálogo de produtos | Cliente |
| RF07 | Selecionar produto para encomenda | Cliente |
| RF08 | Consultar tabela de medidas | Cliente |
| RF09 | Visualizar carrinho de compras | Cliente |
| RF10 | Filtrar produtos por categoria, cor ou material | Cliente |
| RF11 | Adicionar produto ao carrinho | Cliente |
| RF12 | Remover produto do carrinho | Cliente |
| RF13 | Confirmar pedido | Cliente |
| RF14 | Calcular frete | Cliente |
| RF15 | Efetuar pagamento (PIX, cartão de crédito ou boleto) | Cliente |
| RF16 | Visualizar histórico de compras | Cliente |
| RF17 | Confirmar recebimento de pagamento | Admin |
| RF18 | Visualizar pedidos pagos | Admin |
| RF19 | Gerenciar status do pedido | Admin |

---

## 🛠️ Stack tecnológica

### Back-end
- **Java 21**
- **Spring Boot 4.0.6**
  - Spring Web MVC — API REST
  - Spring Data JPA — persistência de dados
  - Spring Security — autenticação e autorização
  - Spring Validation — validação de dados de entrada
  - Spring Kafka — mensageria
- **PostgreSQL 16** — banco de dados de produção
- **H2** — banco em memória para testes
- **Lombok** — redução de boilerplate
- **Maven** — gerenciamento de dependências
- **Docker / Docker Compose** — containerização do banco de dados

### Front-end *(em desenvolvimento separado)*
- **React**
- **React Router DOM**
- **React Hooks**

---

## ✅ Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [Java 21+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker e Docker Compose](https://www.docker.com/)
- [Git](https://git-scm.com/)

---

## 🚀 Como rodar o projeto localmente

### 1. Clonar o repositório

```bash
git clone https://github.com/marikm/encanto.git
cd encanto
```

### 2. Configurar as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
DB_NOME=encanto_db
DB_USUARIO=seu_usuario
DB_SENHA=sua_senha
```

> ⚠️ O arquivo `.env` **não deve ser versionado**. Certifique-se de que ele está no `.gitignore`.

### 3. Subir o banco de dados com Docker

```bash
docker-compose up -d
```

Isso irá subir uma instância do PostgreSQL na porta `5432`.

### 4. Configurar o perfil de execução

O projeto possui dois perfis de configuração:

| Perfil | Banco | Arquivo de configuração |
|--------|-------|------------------------|
| `test` (padrão) | H2 em memória | `application-test.properties` |
| `prod` | PostgreSQL | `application.properties` |

Para rodar em produção, edite `application.properties` e ative as linhas comentadas, ou passe o perfil via linha de comando (passo 5).

### 5. Rodar o back-end

**Perfil de teste (H2 — sem necessidade do Docker):**
```bash
mvn spring-boot:run
```

**Perfil de produção (PostgreSQL):**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

A API estará disponível em: `http://localhost:8080`

> Em modo de teste, o console H2 está acessível em `http://localhost:8080/h2-console`
> com a URL JDBC: `jdbc:h2:mem:testdb` e usuário `sa` (sem senha).

---

## 📁 Estrutura de pastas

```
encanto/
├── src/
│   ├── main/
│   │   ├── java/com/matsumoto/encanto/
│   │   │   ├── config/              # Configurações (ex: seed de dados de teste)
│   │   │   ├── controller/          # Controllers REST
│   │   │   ├── domain/              # Entidades JPA
│   │   │   ├── exceptions/          # Exceções customizadas e GlobalExceptionHandler
│   │   │   ├── repository/          # Interfaces JPA Repository
│   │   │   ├── service/             # Regras de negócio
│   │   │   └── EncantoApplication.java
│   │   └── resources/
│   │       ├── application.properties        # Configuração de produção
│   │       └── application-test.properties   # Configuração de teste (H2)
│   └── test/
│       └── java/com/matsumoto/encanto/
│           └── EncantoApplicationTests.java
├── docker-compose.yml               # Serviço PostgreSQL
├── pom.xml
└── README.md
```

---

## 📊 Status de implementação

### Entidades

| Funcionalidade | Status | Branch |
|---------------|--------|--------|
| Entidade `Categoria` | 🚧 Em desenvolvimento | `main` |
| Entidade `Cor` | ✅ Concluído | `main` |
| Entidade `Material` | ✅ Concluído | `main` |
| Entidade `Produto` | 🚧 Em desenvolvimento | `main` |
| Entidade `Variacao` | 🚧 Em desenvolvimento | `main` |
| Entidade `Cliente` | ⏳ Aguardando | — |
| Entidade `Pedido` | ⏳ Aguardando | — |
| Entidade `ItemPedido` | ⏳ Aguardando | — |
| Entidade `Pagamento` | ⏳ Aguardando | — |

### Endpoints da API

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| `GET` | `/categorias` | Listar todas as categorias | 🚧 Em desenvolvimento |
| `GET` | `/api/cores` | Listar todas as cores | ✅ Concluído |
| `GET` | `/api/cores/{id}` | Buscar cor por ID | ✅ Concluído |
| `POST` | `/categorias` | Cadastrar categoria | ⏳ Aguardando |
| `GET` | `/produtos` | Listar catálogo | ⏳ Aguardando |
| `GET` | `/produtos/{id}` | Detalhar produto | ⏳ Aguardando |
| `POST` | `/produtos` | Cadastrar produto | ⏳ Aguardando |
| `GET` | `/materiais` | Listar materiais | ⏳ Aguardando |
| `POST` | `/pedidos` | Confirmar pedido | ⏳ Aguardando |
| `GET` | `/pedidos/{id}` | Visualizar pedido | ⏳ Aguardando |
| `POST` | `/auth/login` | Autenticar usuário | ⏳ Aguardando |
| `POST` | `/auth/registro` | Cadastrar cliente | ⏳ Aguardando |
| `POST` | `/pagamentos` | Efetuar pagamento | ⏳ Aguardando |

### Infraestrutura

| Funcionalidade | Status | Branch |
|---------------|--------|--------|
| Conexão com banco de dados (prod e teste) | ✅ Concluído | `main` |
| Variáveis de ambiente configuradas | ✅ Concluído | `main` |
| GlobalExceptionHandler | ✅ Concluído | `main` |
| Autenticação com Spring Security | ⏳ Aguardando | — |
| Docker Compose (PostgreSQL) | ✅ Concluído | `main` |

---

## 👩‍💻 Autora

Feito com 🧶 por **Marina Matsumoto**

[![GitHub](https://img.shields.io/badge/GitHub-marikm-181717?style=flat-square&logo=github)](https://github.com/marikm)
[![Email](https://img.shields.io/badge/Email-matsumotomarina3%40gmail.com-D14836?style=flat-square&logo=gmail&logoColor=white)](mailto:matsumotomarina3@gmail.com)
