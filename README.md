<<<<<<< HEAD
# ESG - Inclusão e Diversidade Corporativa

## Execução de Testes Automatizados

O projeto utiliza um conjunto abrangente de testes automatizados, incluindo:
- Testes Unitários
- Testes de Comportamento (BDD) utilizando Cucumber
- Testes de Contrato e API utilizando RestAssured

### Pré-requisitos para Execução

Antes de executar os testes localmente, certifique-se de que o ambiente esteja configurado corretamente:

1. **Java 21**: O projeto requer o Java 21 instalado (verifique com `java -version`).
2. **Banco de Dados Oracle**: Os testes de API e BDD iniciam o contexto do Spring Boot, exigindo a conexão com o banco de dados Oracle. 
   - Certifique-se de ter acesso à rede ou VPN necessária para acessar o servidor `oracle.fiap.com.br`, ou substitua as credenciais pelas de um banco local configurando as variáveis de ambiente:
     - `DB_URL` (ex: `jdbc:oracle:thin:@localhost:1521:orcl`)
     - `DB_USER`
     - `DB_PASS`

### Executando Localmente

Para rodar todos os testes localmente, abra o terminal na raiz do projeto e execute o comando do Gradle correspondente ao seu sistema operacional:

**No Linux, macOS ou Git Bash (Windows):**
```bash
./gradlew test
```

**No Prompt de Comando (CMD) ou PowerShell (Windows):**
```cmd
gradlew test
```

Caso queira ver o relatório completo de testes, ele será gerado em:
`build/reports/tests/test/index.html`

### Execução em Pipeline CI/CD

Este projeto está configurado com **GitHub Actions** (`.github/workflows/ci-cd.yml`).
Sempre que há um *push* ou *pull request* para a branch `main`, o pipeline de CI/CD é acionado.

O passo de `Build and Test with Gradle` executa automaticamente `./gradlew build`, que inclui a execução de todos os nossos testes BDD e testes de API.

Se os testes falharem, o pipeline é interrompido, garantindo que o código defeituoso não seja integrado ou feito deploy nos ambientes de *Staging* ou *Produção*.
=======
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
>>>>>>> aff2036efbcd68508593eaf76c035b63adfc6417
