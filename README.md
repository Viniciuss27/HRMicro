# HRMicro 🚀

Sistema de gestão de RH desenvolvido com arquitetura de microsserviços, utilizando Spring Boot 3.x, JWT, Docker e Spring Cloud.

\---

## 📋 Sobre o Projeto

O HRMicro é um sistema backend de RH construído com microsserviços independentes que se comunicam entre si via API Gateway, com autenticação JWT, descoberta de serviços via Eureka e configurações centralizadas no GitHub.

\---

## 🏗️ Arquitetura

```
Insomnia/Frontend
       ↓
hr-api-gateway (8765)
       ↓
┌──────────────────────────────────────┐
│  hr-oauth   hr-user   hr-worker      │
│  hr-payroll                          │
└──────────────────────────────────────┘
       ↓
hr-eureka-server (8761)
hr-config-server (8888) → GitHub
PostgreSQL (Docker)
```

\---

## 🛠️ Tecnologias

|Tecnologia|Versão|Uso|
|-|-|-|
|Java|21|Linguagem principal|
|Spring Boot|3.2.5|Framework principal|
|Spring Cloud|2023.0.3|Microsserviços|
|Spring Security|6.x|Autenticação e autorização|
|JWT (jjwt)|0.12.5|Tokens de acesso|
|Spring Cloud Gateway|-|API Gateway|
|Netflix Eureka|-|Service Discovery|
|Spring Cloud Config|-|Configurações centralizadas|
|OpenFeign|-|Comunicação entre serviços|
|Resilience4j|-|Circuit Breaker|
|PostgreSQL|16|Banco de dados produção|
|H2|-|Banco de dados testes|
|Docker|-|Containerização|
|Docker Compose|-|Orquestração de containers|

\---

## 📦 Microsserviços

|Serviço|Porta|Descrição|
|-|-|-|
|`hr-api-gateway`|8765|Gateway central — roteia e autentica requisições|
|`hr-eureka-server`|8761|Servidor de descoberta de serviços|
|`hr-config-server`|8888|Configurações centralizadas via GitHub|
|`hr-oauth`|dinâmica|Autenticação e geração de tokens JWT|
|`hr-user`|dinâmica|Gerenciamento de usuários e roles|
|`hr-worker`|dinâmica|Gerenciamento de trabalhadores|
|`hr-payroll`|dinâmica|Cálculo de folha de pagamento|

\---

## 🔐 Segurança

O sistema utiliza autenticação JWT com controle de acesso por roles:

|Role|Acesso|
|-|-|
|`ROLE\_OPERATOR`|`/hr-worker/\*\*`|
|`ROLE\_ADMIN`|`/hr-worker/\*\*`, `/hr-payroll/\*\*`, `/hr-user/\*\*`|

\---

## 🐳 Como Rodar com Docker

### Pré-requisitos

* Docker instalado
* Docker Compose instalado

### 1\. Clone o repositório

```bash
git clone https://github.com/Viniciuss27/HRMicro.git
cd HRMicro
```

### 2\. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto:

```
GIT\_PASSWORD=seu\_token\_github
```

### 3\. Suba todos os containers

```bash
docker-compose up -d
```

### 4\. Aguarde os serviços iniciarem

Verifique se todos estão rodando:

```bash
docker ps
```

Acesse o Eureka Dashboard:

```
http://localhost:8761
```

\---

## 🧪 Testando a API

### 1\. Gerar token JWT

```
POST http://localhost:8765/hr-oauth/oauth/token

Headers:
  client-id: myappname123
  client-secret: myappsecret123

Params:
  email: nina@gmail.com
  password: 123456
```

### 2\. Acessar recursos protegidos

```
GET http://localhost:8765/hr-worker/workers
Authorization: Bearer {token}

GET http://localhost:8765/hr-user/users/1
Authorization: Bearer {token} (requer ROLE\_ADMIN)
```

\---

## 🗂️ Estrutura do Projeto

```
HRMicro/
├── Hr-Api-Gateway/
├── Hr-Config-Server/
├── Hr-Eureka/
├── Hr-Oauth/
├── Hr-Payroll/
├── Hr-User/
├── Hr-Worker/
├── docker-compose.yml
├── .env (não versionado)
└── .gitignore
```

\---

## ⚙️ Configurações Centralizadas

As configurações dos serviços ficam no repositório:
🔗 [HRMicro](https://github.com/Viniciuss27/HRMicro)

Cada serviço busca suas configs automaticamente ao iniciar:

```
hr-user-test.yml   → perfil de testes (H2)
hr-user-dev.yml    → perfil de desenvolvimento (PostgreSQL)
```

\---

## 📊 Fluxo de Autenticação

```
1. Cliente envia email + senha para /hr-oauth/oauth/token
2. hr-oauth valida as credenciais via hr-user (Feign)
3. hr-oauth gera e retorna o token JWT
4. Cliente usa o token nas requisições seguintes
5. hr-api-gateway valida o token e verifica as roles
6. Requisição é roteada para o serviço correto
```

\---

## 👨‍💻 Autor

Desenvolvido por **Vinícius** 🚀

[!\[GitHub](https://img.shields.io/badge/GitHub-Viniciuss27-black?logo=github)](https://github.com/Viniciuss27)

\---

## 📄 Licença

Este projeto está sob a licença MIT.

