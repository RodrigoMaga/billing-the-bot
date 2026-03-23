# 🤖 YouTube Premium Billing Bot

<div align="center">

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.12-green?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

</div>

## 📝 Sobre

Uma **API REST robusta** desenvolvida em Java + Spring Boot para gerenciar participantes e controlar pagamentos mensais de um grupo de assinatura compartilhada de YouTube Premium.

O sistema foi construído com foco em:
- ✅ **Integridade de dados** - Constraints únicos no banco
- ✅ **Arquitetura em camadas** - Separação clara de responsabilidades
- ✅ **RESTful** - Endpoints bem estruturados
- ✅ **Preparado para crescimento** - Pronto para integração com chatbots (WhatsApp)

### Funcionalidades Principais

- 👥 **Cadastro de participantes** com informações de contato
- 💳 **Registro de pagamentos** mensais por participante
- ✔️ **Liquidação de pagamentos** (marcar como pago)
- 🔍 **Consultas paginadas** de pagamentos
- 🔐 **Segurança de dados** com regras de integridade
- 📊 **Filtros avançados** por mês, ano e participante

---

## 🚀 Stack Tecnológico

| Tecnologia | Versão | Descrição |
|-----------|--------|-----------|
| **Java** | 21 | Linguagem principal do projeto |
| **Spring Boot** | 3.2.12 | Framework web e de configuração |
| **Spring Data JPA** | - | Abstração de persistência de dados |
| **Hibernate** | - | ORM para mapeamento de entidades |
| **MySQL** | 8.0+ | Banco de dados relacional |
| **Maven** | 3.8+ | Gerenciador de dependências e build |
| **Swagger/OpenAPI** | 3.0 | Documentação automática de APIs |
| **Docker** | Compose | Containerização da aplicação |
| **JUnit 5** | - | Framework de testes unitários |

---

## 🏗️ Arquitetura

O projeto segue o padrão de **arquitetura em camadas**, garantindo separação de responsabilidades e facilidade de manutenção:

```
┌─────────────────────────────────────────┐
│         HTTP Request/Response            │
├─────────────────────────────────────────┤
│         🎮 Controller Layer              │
│    (REST Endpoints - PaymentController,  │
│      ParticipantController)              │
├─────────────────────────────────────────┤
│      🧠 Service Layer                    │
│  (Business Logic - PaymentService,       │
│   ParticipantService)                    │
├─────────────────────────────────────────┤
│   📊 Repository Layer (JPA)              │
│  (Data Access - PaymentRepository,       │
│   ParticipantRepository)                 │
├─────────────────────────────────────────┤
│    🗄️ Database (MySQL)                   │
│  (Persistent Data Storage)               │
└─────────────────────────────────────────┘
```

### Componentes da Arquitetura

- **Controller**: Recebe requisições HTTP e retorna respostas JSON
- **Service**: Contém toda lógica de negócio e validações
- **Repository**: Acessa dados no banco via Spring Data JPA
- **Entity**: Modelos de domínio mapeados para o banco
- **DTO**: Objetos de transferência de dados (Request/Response)  

---

## 📦 Modelo de Domínio

### Diagrama Entidade-Relacionamento

```
┌─────────────────────┐         ┌──────────────────────┐
│   PARTICIPANT       │         │      PAYMENT         │
├─────────────────────┤         ├──────────────────────┤
│ id (PK)             │◄────────│ id (PK)              │
│ name                │  1 : N  │ month                │
│ email               │         │ year                 │
│ phone               │         │ paymentStatus        │
│ billingOrder        │         │ participant_id (FK)  │
│ createdAt           │         │ createdAt            │
│ updatedAt           │         │ updatedAt            │
└─────────────────────┘         └──────────────────────┘

UNIQUE (participant_id, month, year)
```

### Participant (Participante)

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | Long | Identificador único |
| `name` | String | Nome completo |
| `email` | String | Email para contato |
| `phone` | String | Telefone de contato |
| `billingOrder` | Integer | Ordem de cobrança no grupo |
| `createdAt` | LocalDateTime | Data de criação |
| `updatedAt` | LocalDateTime | Data de atualização |

### Payment (Pagamento)

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | Long | Identificador único |
| `month` | Integer | Mês (1-12) |
| `year` | Integer | Ano (ex: 2026) |
| `paymentStatus` | Enum | PAID ou NOT_PAID |
| `participant` | Participant | Referência ao participante |
| `createdAt` | LocalDateTime | Data de criação |
| `updatedAt` | LocalDateTime | Data de atualização |

---

## 🔒 Regra de Integridade

Existe constraint única no banco:

UNIQUE (participant_id, payment_month, payment_year)

Isso garante que um participante não possa ter dois pagamentos no mesmo mês e ano.

---

## 🔌 API REST - Endpoints Principais

### Participantes

#### Criar Participante
```http
POST /participants
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@example.com",
  "phone": "+55 11 99999-9999",
  "billingOrder": 1
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "phone": "+55 11 99999-9999",
  "billingOrder": 1,
  "createdAt": "2026-03-23T10:30:00",
  "updatedAt": "2026-03-23T10:30:00"
}
```

#### Listar Todos os Participantes
```http
GET /participants?page=0&size=10
```

#### Obter Participante por ID
```http
GET /participants/{id}
```

---

### Pagamentos

#### Criar Pagamento
```http
POST /payments
Content-Type: application/json

{
  "month": 3,
  "year": 2026,
  "paymentStatus": "PAID",
  "participantId": 1
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "month": 3,
  "year": 2026,
  "paymentStatus": "PAID",
  "participant": {
    "id": 1,
    "name": "João Silva"
  },
  "createdAt": "2026-03-23T10:31:00",
  "updatedAt": "2026-03-23T10:31:00"
}
```

#### Listar Pagamentos por Settlement (Mês/Ano)
```http
GET /payments/settlement?month=3&year=2026&page=0&size=10
```

**Resposta:**
```json
{
  "content": [
    {
      "id": 1,
      "month": 3,
      "year": 2026,
      "paymentStatus": "PAID",
      "participant": { "id": 1, "name": "João Silva" }
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "number": 0,
  "size": 10
}
```

#### Listar Pagamentos por Participante
```http
GET /payments/participant/{participantId}?page=0&size=10
```

#### Atualizar Status de Pagamento
```http
PUT /payments/{id}
Content-Type: application/json

{
  "paymentStatus": "PAID"
}
```

#### Deletar Pagamento
```http
DELETE /payments/{id}
```

---

## 📄 Paginação

A API utiliza Pageable do Spring Data:

- `page` → começa em 0  
- `size` → quantidade de registros por página  

Exemplo de retorno:

```json
{
  "content": [...],
  "totalElements": 4,
  "totalPages": 1,
  "number": 0,
  "size": 10
}
```

---

## ✅ Boas Práticas Aplicadas

- Uso de DTOs (não expõe entidades diretamente)
- Paginação com Page<T>
- Validação de regras de negócio no service
- Constraint de integridade no banco
- Separação clara entre camadas

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 21** ou superior
- **Maven 3.8+** ou use o `./mvnw` incluído
- **MySQL 8.0+** (para execução local)
- **Docker & Docker Compose** (apenas para containerização)
- **Git**

---

## ⚡ Quick Start - Execução Local

### 1. Clone o Repositório

```bash
git clone https://github.com/RodrigoMaga/billing-the-bot.git
cd billing-the-bot
```

### 2. Configure o Banco de Dados

O projeto usa MySQL por padrão. Certifique-se de que está rodando em `localhost:3306`:

```sql
CREATE DATABASE youtube_premium_billing_bot;
```

Ou modifique as credenciais em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/youtube_premium_billing_bot
spring.datasource.username=root
spring.datasource.password=sua_senha
```

### 3. Execute a Aplicação

```bash
# Usando Maven
./mvnw spring-boot:run

# Ou se estiver no Windows
mvnw.cmd spring-boot:run
```

A API estará disponível em: **http://localhost:8080**

### 4. Acesse a Documentação Swagger

```
http://localhost:8080/swagger-ui.html
```

---

## 🐳 Execução com Docker Compose

### Pré-requisitos

- Docker instalado
- Docker Compose instalado

### Passos

```bash
# Clone o repositório
git clone https://github.com/RodrigoMaga/billing-the-bot.git
cd billing-the-bot

# Inicie os serviços
docker compose up --build
```

O `docker-compose.yml` inicia automaticamente:
- **MySQL** na porta 3306
- **Aplicação** na porta 8080

**Nota importante:** Quando rodando em Docker Compose, a aplicação conecta ao banco usando o hostname interno `mysql` (não `localhost`).

### Limpar Recursos Docker

```bash
# Parar containers
docker compose down

# Remover volumes do banco (reset total)
docker compose down -v
```

---

