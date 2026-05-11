# Plano de Testes BDD - ESG Diversidade Corporativa

Este documento descreve os cenários de teste escritos em linguagem Gherkin utilizados para validar as funcionalidades do sistema ESG Diversidade Corporativa através de testes de comportamento (BDD).

## Funcionalidade: Gestão de Funcionários (Employee Management)
**Como um** Administrador de RH  
**Eu quero** ser capaz de gerenciar funcionários  
**Para que** eu possa acompanhar a equipe da empresa e seus detalhes

### Cenário 1: Criação de um novo funcionário com sucesso
**Objetivo:** Validar se o sistema permite a criação de um funcionário quando todos os dados obrigatórios são fornecidos corretamente.

*   **Dado** que a aplicação está rodando
*   **Quando** eu solicitar a criação de um novo funcionário com os seguintes detalhes:
    | Campo         | Valor              |
    |---------------|--------------------|
    | employeeId    | EMP001             |
    | name          | John Doe           |
    | email         | john.doe@esg.com   |
    | gender        | M                  |
    | departmentId  | DEP001             |
*   **Então** o status da resposta deve ser 200 (OK)
*   **E** o corpo da resposta deve conter os detalhes do funcionário criado (ID e Nome)

### Cenário 2: Falha ao buscar um funcionário inexistente
**Objetivo:** Validar se o sistema retorna o erro apropriado ao tentar buscar um funcionário que não consta na base de dados.

*   **Dado** que a aplicação está rodando
*   **Quando** eu solicitar os dados do funcionário com o ID "NON_EXISTENT_ID"
*   **Então** o status da resposta deve ser 404 (Not Found)

---

## Funcionalidade: Listagem de Departamentos (Department Listing)
**Como** qualquer funcionário  
**Eu quero** ser capaz de visualizar os departamentos  
**Para que** eu possa conhecer a estrutura organizacional

### Cenário 1: Listagem de departamentos com sucesso
**Objetivo:** Validar se o sistema retorna a lista completa de departamentos cadastrados.

*   **Dado** que a aplicação está rodando
*   **Quando** eu solicitar a listagem de todos os departamentos
*   **Então** o status da resposta deve ser 200 (OK)
*   **E** a resposta deve ser uma lista (mesmo que vazia)

---

## Execução Técnica
Os testes são executados automaticamente através do framework **Cucumber** integrado ao **Spring Boot**.
A infraestrutura de teste utiliza:
- **RestAssured:** Para chamadas HTTP e validações de resposta.
- **H2 Database:** Banco de dados in-memory para isolamento dos testes.
- **Cucumber JUnit Platform Engine:** Para integração com o ciclo de vida de testes do Gradle.

Para rodar estes cenários localmente:
```bash
./gradlew test
```
