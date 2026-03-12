# YouTube Premium Billing Bot

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de participantes e controle de pagamentos mensais de um grupo de assinatura compartilhada.

O sistema permite:

- Cadastro de participantes
- Registro de pagamentos mensais
- Liquidação (baixa) de pagamentos pendentes
- Consulta paginada de pagamentos
- Garantia de integridade via constraint única no banco

---

## 🚀 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Swagger / OpenAPI
- Docker / Docker Compose

---

## 🏗 Arquitetura

O projeto segue arquitetura em camadas:

controller → service → repository → database

- **Controller**: expõe endpoints REST  
- **Service**: contém regras de negócio  
- **Repository**: acesso ao banco via JPA  
- **DTOs**: isolamento entre entidade e resposta da API  

---

## 📦 Modelo de Domínio

### Participant

- id
- name
- email
- phone
- billingOrder

### Payment

- id
- month
- year
- paymentStatus (PAID | NOT_PAID)
- participant

---

## 🔒 Regra de Integridade

Existe constraint única no banco:

UNIQUE (participant_id, payment_month, payment_year)

Isso garante que um participante não possa ter dois pagamentos no mesmo mês e ano.

---

## 📌 Endpoints Principais

### Criar Pagamento

POST /payments

Request:

```json
{
  "month": 2,
  "year": 2026,
  "paymentStatus": "PAID",
  "participantId": 1
}
```

---

### Listar Pagamentos por Mês e Ano (Paginado)

GET /payments/settlement?month=2&year=2026&page=0&size=10

---

### Listar Pagamentos por Participante (Paginado)

GET /payments/participant/{id}?page=0&size=10

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

## ⚙️ Como Executar Localmente

O arquivo `src/main/resources/application.properties` usa por padrão:

- host `localhost`
- porta `3306`
- banco `youtube_premium_billing_bot`

Então, para rodar pelo IntelliJ ou Maven localmente, basta ter um MySQL disponível na sua máquina com essas credenciais, ou sobrescrever:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Exemplo:

```bash
./mvnw spring-boot:run
```

---

## 🐳 Como Executar com Docker Compose

O `docker-compose.yml` sobe dois serviços:

- `mysql`
- `youtube-bot`

Nesse cenário, a aplicação **não usa `localhost`** para o banco. Ela usa o hostname interno da rede Docker:

- `jdbc:mysql://mysql:3306/youtube_premium_billing_bot`

Para subir:

```bash
docker compose up --build
```

Se quiser recriar tudo do zero, incluindo volume do banco:

```bash
docker compose down -v
docker compose up --build
```

---

## 📚 Documentação Swagger

Após iniciar a aplicação:

http://localhost:8080/

---

## 📝 Observações

- Rodando localmente via IntelliJ, o banco costuma ser acessado por `localhost`
- Rodando via Docker Compose, o banco deve ser acessado pelo nome do serviço `mysql`
- O erro `Communications link failure` normalmente indica host/porta incorretos ou banco ainda não disponível no momento da conexão
- O projeto está preparado para futura integração com chatbot (WhatsApp)
