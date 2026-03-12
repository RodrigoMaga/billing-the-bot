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

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Swagger / OpenAPI

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

## ⚙️ Como Executar

1. Clone o repositório:

git clone https://github.com/RodrigoMaga/youtube-premium-billing-bot.git

2. Configure o `application.properties` com seu banco MySQL.

3. Execute o projeto:

mvn spring-boot:run

ou

./mvnw spring-boot:run

---

## 📚 Documentação Swagger

Após iniciar a aplicação:

http://localhost:8080/swagger-ui/index.html

---

## 📝 Observações

- Projeto focado em backend Java
- Preparado para futura integração com chatbot (WhatsApp)
- Estrutura escalável para novas funcionalidades

