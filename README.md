# ESG - Inclusão e Diversidade Corporativa

Este repositório contém o projeto **ESG Diversidade Corporativa**, uma aplicação voltada para iniciativas de inclusão e diversidade. Foi construído utilizando tecnologias modernas no ecossistema Java e containerização.

## Tecnologias e Ferramentas

* **Linguagem:** Java 21
* **Framework Principal:** Spring Boot (versão 3.5.7)
* **Gerenciador de Dependências e Build:** Gradle
* **Persistência e ORM:** Spring Data JPA / Hibernate
* **Banco de Dados (Produção):** Oracle Database (via Docker `gvenzl/oracle-free`)
* **Banco de Dados (Testes):** H2 Database (in-memory)
* **Migrações de Banco de Dados:** Flyway
* **Containerização e Orquestração:** Docker e Docker Compose
* **Testes:** JUnit 5, Cucumber (BDD), RestAssured (API)
* **Utilitários:** Lombok, Spring Boot Actuator

## Arquitetura do Projeto

O projeto segue a clássica arquitetura em camadas do Spring, promovendo a separação de responsabilidades:

* **Controller (`controller/`):** Camada responsável por expor as APIs REST, lidar com as requisições HTTP e retornar as respostas adequadas.
* **Service (`service/`):** Camada responsável por encapsular as regras de negócio da aplicação. Atua como intermediária entre os Controllers e os Repositories.
* **Repository (`repository/`):** Camada de acesso a banco de dados, utilizando as abstrações do Spring Data JPA para operações de CRUD e consultas personalizadas.
* **Entity (`entity/`):** Classes de modelo (domínio) mapeadas para as tabelas do banco de dados (usando anotações do JPA).
* **DTO (`dto/`):** Data Transfer Objects, usados para transferir dados entre as camadas, controlando o que entra e sai nas respostas da API.

---

## Testes Automatizados

O projeto utiliza um conjunto abrangente de testes automatizados:

- **Testes Unitários** — Validam a lógica das camadas de serviço usando JUnit 5 e Mockito.
- **Testes de Comportamento (BDD)** — Cenários Gherkin executados pelo Cucumber, que validam fluxos completos da aplicação.
- **Testes de Contrato e API** — Utilizam RestAssured para verificar status HTTP, corpo das respostas JSON e validação de JSON Schema.

### Banco de Dados para Testes (H2 in-memory)

Os testes **não dependem do banco Oracle** para serem executados. O projeto utiliza o **H2 Database** em modo in-memory durante os testes, configurado em `src/test/resources/application.properties`:

```properties
# H2 in-memory database para testes
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=Oracle
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Hibernate cria as tabelas automaticamente a partir das entidades
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop

# Flyway desabilitado nos testes (migrations Oracle não são compatíveis com H2)
spring.flyway.enabled=false
```

Isso significa que:
- O Hibernate cria e destrói as tabelas automaticamente (`create-drop`) com base nas entidades JPA.
- Não é necessário acesso a nenhum banco externo, VPN, ou Docker rodando.
- Os testes funcionam em qualquer máquina com Java 21 instalado.

### Pré-requisitos para Execução dos Testes

1. **Java 21** instalado e configurado no `PATH` (verifique com `java -version`).
2. **Nenhum banco de dados** é necessário — os testes usam o H2 in-memory automaticamente.

### Executando os Testes Localmente

Abra o terminal na raiz do projeto e execute:

**Linux, macOS ou Git Bash (Windows):**
```bash
./gradlew test
```

**PowerShell ou CMD (Windows):**
```cmd
gradlew test
```

> **⚠️ Nota:** Se o seu sistema tiver uma versão do Java diferente de 21 como padrão, aponte o `JAVA_HOME` para o Java 21 antes de rodar:
> ```bash
> # Linux/macOS
> export JAVA_HOME=/caminho/para/java-21
> ./gradlew test
> ```
> ```powershell
> # PowerShell (Windows)
> $env:JAVA_HOME = "C:\caminho\para\java-21"
> .\gradlew test
> ```

O relatório HTML dos testes será gerado em:
```
build/reports/tests/test/index.html
```

### Execução em Pipeline CI/CD

Este projeto está configurado com **GitHub Actions** (`.github/workflows/ci-cd.yml`).
Sempre que há um *push* ou *pull request* para a branch `main`, o pipeline de CI/CD é acionado automaticamente.

O passo `Build and Test with Gradle` executa `./gradlew build`, que inclui todos os testes BDD, de API e unitários. Se os testes falharem, o pipeline é interrompido, impedindo que código defeituoso seja integrado ou feito deploy nos ambientes de *Staging* ou *Produção*.

---

## Infraestrutura e Configuração (Produção)

A aplicação é executada junto com seu banco de dados utilizando **Docker Compose**. O arquivo `docker-compose.yml` declara dois serviços:

1. **`db` (Banco de Dados Oracle):**
   - Imagem: `gvenzl/oracle-free:latest`
   - Porta: `1521`
   - Credenciais configuradas via variáveis de ambiente (arquivo `.env`)
   - Healthcheck configurado para garantir disponibilidade

2. **`app` (Aplicação Spring Boot):**
   - Construída a partir do `Dockerfile` na raiz
   - Porta: `8080`
   - Aguarda o banco estar saudável (`service_healthy`) antes de iniciar
   - Executa migrações Flyway automaticamente na subida

### Como Executar a Aplicação

#### Pré-requisitos

* **Docker** e **Docker Compose** instalados
* Portas `8080` (App) e `1521` (Oracle) livres

#### Passos

1. Crie ou verifique o arquivo `.env` na raiz do projeto:
   ```env
   DB_ROOT_PASSWORD=sua_senha_sys
   DB_APPLICATION_USER=seu_usuario
   DB_APPLICATION_PASSWORD=sua_senha_usuario
   ```

2. Suba os contêineres:
   ```bash
   docker-compose up -d --build
   ```

3. Aguarde o healthcheck do Oracle. Após a confirmação, a aplicação subirá automaticamente.

4. A API estará acessível em `http://localhost:8080`.

5. Para derrubar os contêineres:
   ```bash
   docker-compose down
   ```
