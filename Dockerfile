# Estágio 1: Build da aplicação
FROM gradle:8-jdk21 AS build
WORKDIR /app

# Copia os arquivos de configuração do Gradle primeiro para aproveitar o cache de camadas
COPY build.gradle settings.gradle ./
COPY src ./src

# Gera o JAR da aplicação (ignora testes para acelerar o build se desejar)
RUN ./gradlew bootJar --no-daemon

# Estágio 2: Runtime leve
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia apenas o artefato gerado no estágio anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
