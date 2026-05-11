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
