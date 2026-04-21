# ESG Diversidade Corporativa

Este repositório contém o projeto **ESG Diversidade Corporativa**, uma aplicação voltada para iniciativas de inclusão e diversidade. Foi construído utilizando tecnologias modernas no ecossistema Java e containerização.

## Tecnologias e Ferramentas

* **Linguagem:** Java 21
* **Framework Principal:** Spring Boot (versão 3.5.7)
* **Gerenciador de Dependências e Build:** Gradle
* **Persistência e ORM:** Spring Data JPA / Hibernate
* **Banco de Dados:** Oracle Database (via Docker `gvenzl/oracle-free`)
* **Migrações de Banco de Dados:** Flyway
* **Containerização e Orquestração:** Docker e Docker Compose
* **Utilitários e Ferramentas:** Lombok (redução de boilerplate), Spring Boot Actuator (monitoramento) e JUnit (Testes)

## Arquitetura do Projeto

O projeto segue a clássica arquitetura em camadas do Spring, promovendo a separação de responsabilidades e facilitando a manutenção e a escalabilidade:

* **Controller (`controller/`):** Camada responsável por expor as APIs REST, lidar com as requisições HTTP e retornar as respostas adequadas. Onde os endpoints (rotas) são definidos.
* **Service (`service/`):** Camada responsável por encapsular as regras de negócio da aplicação. Ela atua como intermediária entre os Controllers e os Repositories.
* **Repository (`repository/`):** Camada de acesso a banco de dados, utilizando as abstrações do Spring Data JPA para realizar operações de CRUD e consultas personalizadas nas entidades do banco.
* **Entity (`entity/`):** Classes de modelo (domínio) mapeadas para as tabelas do banco de dados (usando as anotações do JPA).
* **DTO (`dto/`):** Data Transfer Objects, objetos usados para transferir dados entre as camadas, controlando exatamente o que entra e o que sai nas respostas da API (ocultando detalhes internos das Entities).

## Infraestrutura e Configuração

A aplicação é executada junto com seu banco de dados utilizando **Docker Compose**. O arquivo `docker-compose.yml` declara dois serviços principais:

1. **`db` (Banco de Dados):**
   - Utiliza a imagem `gvenzl/oracle-free:latest`.
   - Mapeado na porta `1521`.
   - Utiliza as variáveis de ambiente baseadas no arquivo `.env` para as credenciais (`ORACLE_PASSWORD`, `APP_USER`, `APP_USER_PASSWORD`).
   - Possui um script de *healthcheck* configurado.

2. **`app` (Aplicação Spring Boot):**
   - Construída a partir do `Dockerfile` localizado na raiz.
   - Mapeada na porta `8080`.
   - Depende de o banco de dados estar saudável (`condition: service_healthy`) para inicializar.
   - Conecta-se ao banco e roda as migrações (Flyway) automaticamente na subida.

## Como Executar o Projeto

### Pré-requisitos

* Ter o **Docker** e o **Docker Compose** instalados na sua máquina.
* Garantir que as portas `8080` (App) e `1521` (Oracle) estejam livres.

### Passos

1. Crie ou verifique o arquivo `.env` na raiz do projeto com as credenciais do banco de dados:
   ```env
   ORACLE_PASSWORD=sua_senha_sys
   APP_USER=seu_usuario
   APP_USER_PASSWORD=sua_senha_usuario
   ```
2. Na raiz do projeto, execute o comando do Docker Compose para construir a imagem e subir os contêineres em segundo plano:
   ```bash
   docker-compose up -d --build
   ```
3. O Docker iniciará primeiramente o container do banco (`oracle_db`). Após o healthcheck confirmar que o Oracle está pronto para receber conexões, o serviço da aplicação (`esg_app`) subirá.
4. A API estará acessível em `http://localhost:8080`.
5. Para derrubar os contêineres, execute:
   ```bash
   docker-compose down
   ```

## Testes

O projeto conta com o **JUnit Platform** integrado para execução de testes automatizados, que podem ser executados através da task de teste do Gradle:
```bash
./gradlew test
```
