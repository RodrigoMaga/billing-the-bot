# 🤖 Billing The Bot

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
- ✅ **Notificações automáticas** - Integração com email para avisos de pagamento
- ✅ **Containerizado** - Pronto para Docker e deploy em produção

### Funcionalidades Principais

- 👥 **Cadastro de participantes** com informações de contato
- 💳 **Registro de pagamentos** mensais por participante
- ✔️ **Liquidação de pagamentos** (marcar como pago)
- 📧 **Notificações por email** para participantes
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
| **Spring Mail** | - | Envio de emails e notificações |
| **Hibernate** | - | ORM para mapeamento de entidades |
| **MySQL** | 8.0+ | Banco de dados relacional |
| **Maven** | 3.8+ | Gerenciador de dependências e build |
| **Swagger/OpenAPI** | 3.0 | Documentação automática de APIs |
| **Thymeleaf** | - | Template engine para emails HTML |
| **Flyway** | 9.22.3 | Versionamento e migrations de banco de dados |
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
│   ParticipantService, EmailService)      │
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
- **Service**: Contém lógica de negócio, validações e envio de emails
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
│ notificationEnable  │         │ createdAt            │
│ createdAt           │         │ updatedAt            │
│ updatedAt           │         └──────────────────────┘
└─────────────────────┘

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
| `notificationEnable` | Boolean | Notificações habilitadas |
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

## 📊 Database Migrations com Flyway

O projeto utiliza **Flyway** para versionamento e controle automático de migrations do banco de dados. Isso garante que todas as mudanças no schema sejam rastreáveis e executadas de forma consistente em todos os ambientes.

### Localização das Migrations

```
src/main/resources/db/migration/
├── V1__Create_participant_and_payment_tables.sql
├── V2__Add_new_feature.sql (exemplo para futuro)
└── ...
```

### Convenção de Naming

As migrations seguem o padrão Flyway obrigatório:

```
V<VERSION>__<DESCRIPTION>.sql
```

**Exemplos válidos:**
- ✅ `V1__Create_participant_and_payment_tables.sql`
- ✅ `V2__Add_notification_date_to_payment.sql`
- ✅ `V3__Create_audit_table.sql`

**Importante:** Use **dois underscores (`__`)** entre a versão e a descrição!

### Adicionando Uma Nova Migration

Quando precisar modificar o schema do banco:

1. Crie um novo arquivo em `src/main/resources/db/migration/`
2. Use o próximo número de versão (exemplo: se a última é V1, use V2)
3. Escreva os comandos SQL (ALTER TABLE, CREATE INDEX, etc)
4. Faça commit e deploy - Flyway executará automaticamente!

**Exemplo de V2:**
```sql
-- V2__Add_notification_date_to_payment.sql
ALTER TABLE payment ADD COLUMN notification_sent_at DATETIME DEFAULT NULL;
```

### Histórico de Migrations

Flyway mantém um histórico em uma tabela especial `flyway_schema_history`:

```sql
SELECT * FROM flyway_schema_history;
```

Cada linha rastreia:
- **version**: Versão executada
- **installed_on**: Data e hora da execução
- **execution_time**: Tempo em ms
- **success**: Status (1 = sucesso, 0 = falha)

### Baseline para Schemas Existentes

Se você estiver migrando um projeto que já tinha o banco criado (sem Flyway), o projeto está configurado com `baselineOnMigrate=true` no `application.properties`. Isso significa:

- ✅ Se o schema já existir, Flyway marcará como "baseline" (versão 0)
- ✅ Depois executa as novas migrations normalmente
- ✅ Sem conflitos ou erros!

```properties
# application.properties
spring.flyway.baselineOnMigrate=true
spring.flyway.baselineVersion=0
```

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
CREATE DATABASE billing_bot;
```

Ou crie um arquivo `.env` na raiz do projeto com as credenciais:

```env
DB_URL=jdbc:mysql://localhost:3306/billing_bot?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=sua_senha
```

As variáveis do `.env` serão carregadas automaticamente pelo `application.properties`.

### 3. Configure Email (Opcional)

Adicione as variáveis de email ao arquivo `.env`:

```env
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USERNAME=seu_email@gmail.com
EMAIL_PASSWORD=sua_senha_app
```

**Nota:** Para Gmail, use uma [senha de app específica](https://support.google.com/accounts/answer/185833) em vez da senha da conta.

### 4. Execute a Aplicação

```bash
# Usando Maven Wrapper
./mvnw spring-boot:run

# Ou se estiver no Windows
mvnw.cmd spring-boot:run

# Ou com Maven instalado
mvn spring-boot:run
```

A API estará disponível em: **http://localhost:8080**

### 5. Acesse a Documentação Swagger

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

# Crie um arquivo .env com as variáveis (veja seção "Variáveis de Ambiente")
# Exemplo: DB_URL, DB_USERNAME, DB_PASSWORD, EMAIL_USERNAME, EMAIL_PASSWORD

# Inicie os serviços
docker compose up -d
```

O `docker-compose.yml` inicia automaticamente:
- **MySQL 8.0** na porta 3307
- **Aplicação Spring Boot** na porta 8080

A imagem da aplicação é baixada automaticamente do Docker Hub (`rodrigomaga/billing-the-bot:latest`).

As variáveis do arquivo `.env` são automaticamente passadas para os containers.

### Download Manual da Imagem (Opcional)

Se preferir baixar a imagem antes de executar:

```bash
docker pull rodrigomaga/billing-the-bot:latest
```

### Acessar a Aplicação

Após iniciar, acesse:

- **API:** http://localhost:8080
- **Swagger/OpenAPI:** http://localhost:8080/swagger-ui.html

### Parar os Serviços

```bash
docker compose down
```

### Remover Volumes (Reset Total)

```bash
docker compose down -v
```

Isso remove containers, networks e volumes do banco de dados.

### Verificar Status dos Containers

```bash
docker compose ps
```

Ambos os serviços devem estar com status `running` e health `healthy`.

---

## 🧪 Testes

Execute os testes unitários:

```bash
./mvnw test
```

Ou com Maven instalado:

```bash
mvn test
```

---

## 📝 Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
# Database
DB_URL=jdbc:mysql://localhost:3306/billing_bot?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=sua_senha

# JPA
JPA_SHOW_SQL=false
JPA_FORMAT_SQL=false

# Email
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USERNAME=seu_email@gmail.com
EMAIL_PASSWORD=sua_senha_app
```

**Nota:** O arquivo `.env` é carregado automaticamente pelo `application.properties`. Adicione `.env` ao `.gitignore` para evitar exposição de credenciais:

```bash
echo ".env" >> .gitignore
```

---

## 🔐 Segurança

- ✅ Validação de entrada em todos os endpoints
- ✅ Constraints de integridade no banco de dados
- ✅ Proteção contra SQL Injection via JPA
- ✅ Mensagens de erro genéricas para falhas
- ✅ Variáveis sensíveis em arquivo de ambiente

---

## 📂 Estrutura do Projeto

```
billing-the-bot/
├── src/
│   ├── main/
│   │   ├── java/com/rodmag/youtube_premium_billing_bot/
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/             # Business Logic
│   │   │   ├── repository/          # Data Access (JPA)
│   │   │   ├── entity/              # Domain Models
│   │   │   ├── exception/           # Custom Exceptions
│   │   │   ├── resource/            # Additional Resources
│   │   │   └── BillingBotApplication.java # Main Application
│   │   └── resources/
│   │       ├── application.properties   # Configuration (Properties)
│   │       ├── application.yaml        # Application Name
│   │       ├── db/migration/            # Flyway Migrations
│   │       │   └── V1__Create_participant_and_payment_tables.sql
│   │       └── templates/               # Email Templates (Thymeleaf)
│   └── test/
│       └── java/com/rodmag/         # Unit Tests
├── docker-compose.yml               # Docker Compose Configuration
├── Dockerfile                       # Docker Build
├── pom.xml                          # Maven Dependencies
├── README.md                        # This File
└── LICENSE                          # MIT License
```

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📞 Suporte

Para dúvidas ou sugestões, abra uma [issue](https://github.com/RodrigoMaga/billing-the-bot/issues) no repositório.

---

## 📄 Licença

Este projeto está sob a licença [MIT](LICENSE). Veja o arquivo `LICENSE` para mais detalhes.

---

## 👨‍💻 Autor

**Rodrigo Magalhães Nunes**
- GitHub: [@RodrigoMaga](https://github.com/RodrigoMaga)
- Email: rgn.nunnes@gmail.com

---

**Última atualização:** Março de 2026
