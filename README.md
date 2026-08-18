# ClinicaAPI

## Sobre o projeto
O ClinicaAPI é uma API REST para gerenciamento de clínicas, permitindo administrar usuários, pacientes, médicos, especialidades e consultas, com autenticação JWT e documentação via Swagger.

## Tecnologias
- Java 17
- Spring Boot
- Spring Security
- JWT
- PostgreSQL
- JPA / Hibernate
- MapStruct
- Lombok
- Swagger / OpenAPI

## Funcionalidades
- Cadastro de pacientes
- Cadastro de médicos
- Cadastro de especialidades
- Agendamento de consultas
- Autenticação com JWT
- Bloqueio após tentativas de login

## Configuração
Crie um arquivo .env na raiz do projeto:

.env
DB_URL=sua_url_do__seu_banco
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
JWT_SECRET=seu_secret

## Como executar
## Pré-requisitos
- Java 17
- Maven
- PostgreSQL
- Git
## Passo a passo
- Clone o repositório:
- git clone https://github.com/gbriel019/ClinicaAPI.git
- Acesse a pasta do projeto:
- cd ClinicaAPI
- Crie um arquivo .env na raiz do projeto e configure as variáveis de ambiente necessárias.
- Configure o banco de dados PostgreSQL.
- Execute a aplicação:
- .\mvnw.cmd spring-boot:run
- Após iniciar, a API estará disponível em:
- http://localhost:8080

## Swagger

A documentação interativa da API pode ser acessada em:

http://localhost:8080/swagger-ui/index.html

## Documentação da API
A API possui documentação interativa utilizando Swagger/OpenAPI.

Com a aplicação em execução, acesse:

http://localhost:8080/swagger-ui/index.html

Através do Swagger é possível visualizar e testar os endpoints da API, incluindo:

- Autenticação e geração de token JWT
- Usuários
- Pacientes
- Médicos
- Especialidades
- Consultas

Os endpoints protegidos exigem autenticação através de um token JWT.

## Autor
Gabriel Pinheiro
