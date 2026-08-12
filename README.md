# Task Manager API
 
API REST para gerenciamento de tarefas em times, desenvolvida com **Spring Boot** como projeto de portfólio, aplicando os conceitos de Spring Web, Spring Data JPA, Migrations com Flyway e Spring Security com autenticação/autorização via JWT.
 
O sistema permite que usuários sejam organizados em times, com dois papéis (**líder** e **integrante**), onde líderes podem criar tarefas e promover outros usuários, e cada usuário só tem acesso às tarefas do seu próprio time.
 
---
 
## Índice
 
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Modelagem do domínio](#modelagem-do-domínio)
- [Regras de negócio e segurança](#regras-de-negócio-e-segurança)
- [Arquitetura do projeto](#arquitetura-do-projeto)
- [Como rodar o projeto](#como-rodar-o-projeto)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [Endpoints da API](#endpoints-da-api)
- [Migrations](#migrations)
---
 
## Tecnologias utilizadas
 
- **Java 21**
- **Spring Boot** (Web, Data JPA, Security)
- **PostgreSQL** como banco de dados
- **Flyway** para versionamento de schema (migrations)
- **JWT** (biblioteca `jjwt`) para autenticação stateless
- **BCrypt** para hash de senhas
- **Lombok** para redução de boilerplate
- **Docker e Docker Compose** para conteinerização
- **Maven** como gerenciador de dependências
---
 
## Modelagem do domínio
 
O sistema é composto por três entidades principais:
 
- **Time**: possui nome único, e agrupa usuários e tarefas.
- **Usuário**: pertence a um time, possui uma role (`LIDER` ou `INTEGRANTE`), e pode ser responsável por tarefas.
- **Tarefa**: pertence a um time e possui um usuário responsável (que precisa pertencer ao mesmo time da tarefa), além de um status (`PENDENTE`, `EM_ANDAMENTO`, `CONCLUIDA`).
### Relacionamentos
 
- Um **Time** possui vários **Usuários** e várias **Tarefas**.
- Um **Usuário** pertence a um único **Time** e pode ser responsável por várias **Tarefas**.
- Uma **Tarefa** pertence a um **Time** e tem um **Usuário** responsável, que obrigatoriamente pertence ao mesmo time da tarefa.
### Integridade referencial
 
Ao excluir um **Time** ou **Usuário**, todas as tarefas relacionadas são excluídas automaticamente em cascata (`ON DELETE CASCADE`), garantido diretamente no banco de dados.
 
---
 
## Regras de negócio e segurança
 
- Senhas são armazenadas com hash **BCrypt**, nunca em texto puro.
- Autenticação é feita via **JWT**, com expiração configurável.
- Todo usuário é criado por padrão com a role `INTEGRANTE`; a promoção a `LIDER` é feita por um endpoint dedicado, acessível apenas a quem já é líder.
- Um usuário responsável por uma tarefa precisa obrigatoriamente pertencer ao mesmo time da tarefa (validado na criação).
- Apenas o **líder do próprio time** pode criar tarefas para aquele time.
- Cada usuário só pode visualizar as tarefas do **seu próprio time**, não de outros times.
- As rotas de **cadastro de usuário** (`POST /api/usuarios`) e **login** (`POST /api/auth`) são públicas; todas as demais exigem um token JWT válido.
---
 
## Arquitetura do projeto
 
O projeto segue uma separação em camadas tradicional do ecossistema Spring:
 
```
com.task.manager
├── Config              # Classes de configuração (ex: PasswordEncoder, SecurityFilterChain)
├── Security            # Lógica de autenticação/autorização (UserDetails, TokenService, filtro JWT)
├── Controller           # Endpoints REST
├── Service              # Regras de negócio
├── DataBase
│   ├── Model            # Entidades JPA
│   ├── Repository       # Interfaces Spring Data JPA
│   └── Enums            # Enums de domínio (StatusType, RoleType)
├── Dto
│   ├── request           # DTOs de entrada
│   └── response          # DTOs de saída
└── Exception             # Exceptions customizadas e handler global
```
 
- **DTOs** são usados em toda a API para evitar expor entidades JPA diretamente (e principalmente, para nunca expor a senha do usuário).
- **Queries com `JOIN FETCH`** são usadas nos pontos de listagem para evitar o problema de N+1 queries, já que os relacionamentos são mapeados como `LAZY`.
- Exceções de negócio (`NotFoundException`, `BadRequestException`) são tratadas globalmente via `@RestControllerAdvice`, retornando os status HTTP corretos (404, 400).
---
 
## Como rodar o projeto
 
O projeto está conteinerizado com Docker, então **não é necessário ter Java, Maven ou PostgreSQL instalados** na máquina — apenas o Docker.
 
### Pré-requisitos
 
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e em execução
### Passo a passo
 
1. **Clone o repositório**
```bash
   git clone <url-do-repositorio>
   cd <pasta-do-projeto>
```
 
2. **Crie o arquivo `.env`** na raiz do projeto (mesmo diretório do `docker-compose.yml`), com o seguinte conteúdo:
```env
   SECRET_KEY=uma-string-aleatoria-com-pelo-menos-32-caracteres
   EXPIRATION=7200000
```
 
   > `SECRET_KEY` é usada para assinar os tokens JWT — deve ser uma string longa e aleatória (mínimo 32 caracteres).
   > `EXPIRATION` é o tempo de validade do token em milissegundos (o valor acima equivale a 2 horas).
   >
   > Esse arquivo **não é versionado no Git** por conter dados sensíveis. Cada pessoa que for rodar o projeto deve criar o seu próprio `.env`.
 
3. **Suba os containers**
   Na raiz do projeto, execute:
```bash
   docker compose up --build
```
 
   Esse comando irá:
   - Construir a imagem da aplicação Spring Boot a partir do `Dockerfile`
   - Subir um container PostgreSQL
   - Subir o container da aplicação, já conectado ao banco
   - Executar automaticamente as migrations do Flyway, criando todo o schema do banco
4. **Verifique se subiu corretamente**
   Nos logs, você deve ver uma mensagem semelhante a:
```
   Tomcat started on port 8080 (http)
   Started TaskManagerApplication in X seconds
```
 
5. **A API estará disponível em:**
```
   http://localhost:8080
```
 
### Parando os containers
 
```bash
docker compose down
```
 
Para derrubar os containers **e apagar os dados do banco** (útil para simular um ambiente "do zero"):
 
```bash
docker compose down -v
```
 
---
 
## Variáveis de ambiente
 
| Variável       | Descrição                                             | Exemplo                  |
|----------------|--------------------------------------------------------|---------------------------|
| `SECRET_KEY`   | Chave usada para assinar e validar os tokens JWT       | string aleatória (32+ caracteres) |
| `EXPIRATION`   | Tempo de expiração do token, em milissegundos           | `7200000` (2 horas)       |
 
As demais variáveis de conexão com o banco (URL, usuário, senha) já estão pré-configuradas no `docker-compose.yml` para o ambiente conteinerizado, e não precisam ser alteradas para rodar localmente via Docker.
 
---
 
## Endpoints da API
 
### Autenticação (público)
 
| Método | Rota          | Descrição                          |
|--------|---------------|-------------------------------------|
| POST   | `/api/auth`   | Login — retorna um token JWT        |
 
### Usuários
 
| Método | Rota                                | Acesso                     | Descrição                              |
|--------|--------------------------------------|-----------------------------|------------------------------------------|
| POST   | `/api/usuarios`                     | Público                     | Cadastro de usuário (role padrão: `INTEGRANTE`) |
| GET    | `/api/usuarios`                     | Autenticado                 | Lista todos os usuários                 |
| GET    | `/api/usuarios/{email}`             | Autenticado                 | Busca usuário por e-mail                |
| DELETE | `/api/usuarios/{email}`             | Autenticado                 | Remove um usuário                        |
| PUT    | `/api/usuarios/{email}/promover`    | Somente `LIDER`             | Promove um usuário a líder do seu time  |
 
### Times
 
| Método | Rota                | Acesso        | Descrição                     |
|--------|---------------------|----------------|---------------------------------|
| POST   | `/api/time`         | Autenticado    | Cria um novo time               |
| GET    | `/api/time`         | Autenticado    | Lista todos os times            |
| GET    | `/api/time/{nome}`  | Autenticado    | Busca time por nome             |
| DELETE | `/api/time/{nome}`  | Autenticado    | Remove um time                  |
 
### Tarefas
 
| Método | Rota                     | Acesso                                  | Descrição                                             |
|--------|---------------------------|-------------------------------------------|----------------------------------------------------------|
| POST   | `/api/tarefas`            | Somente `LIDER` do time da tarefa         | Cria uma nova tarefa                                    |
| GET    | `/api/tarefas/meu-time`   | Autenticado                               | Lista as tarefas do time do usuário autenticado         |
| DELETE | `/api/tarefas/{id}`       | Autenticado                               | Remove uma tarefa por ID                                 |
 
> Todas as rotas autenticadas exigem o header `Authorization: Bearer <token>`, obtido através do endpoint de login.
 
---
 
## Migrations
 
O versionamento do schema é feito via Flyway, com as seguintes migrations aplicadas em ordem:
 
| Versão | Descrição                                       |
|--------|---------------------------------------------------|
| V1     | Criação das tabelas `time`, `usuario` e `tarefa`  |
| V2     | Adiciona constraint de nome único para `time`     |
| V3     | Adiciona `ON DELETE CASCADE` nas FKs de `tarefa`  |
| V4     | Adiciona a coluna `role_type` em `usuario`        |
 
As migrations são executadas automaticamente na inicialização da aplicação, tanto localmente quanto via Docker.
 
---
 
## Autor
 
Projeto desenvolvido por Gabriel Fujarra como parte de um portfólio de estudos em desenvolvimento backend com Java e Spring Boot.
 
- GitHub: [github.com/GabrielFujarra](https://github.com/GabrielFujarra)
- LinkedIn: [linkedin.com/in/gabriel-fujarra](https://linkedin.com/in/gabriel-fujarra)
 

Projeto desenvolvido por Gabriel Fujarra como parte de um portfólio de estudos em desenvolvimento backend com Java e Spring Boot.

GitHub: github.com/GabrielFujarra
LinkedIn: linkedin.com/in/gabriel-fujarra
