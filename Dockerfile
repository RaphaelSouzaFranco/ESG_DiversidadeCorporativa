# Estágio 1: Build da aplicação
FROM gradle:8-jdk21 AS build
WORKDIR /app

# Copia o wrapper primeiro (melhora cache)
COPY gradlew ./
COPY gradle ./gradle

# Dá permissão antes de usar
RUN chmod +x gradlew

# Copia arquivos de build
COPY build.gradle settings.gradle ./

# Baixa dependências (cache eficiente)
RUN ./gradlew dependencies --no-daemon

# Copia o código fonte
COPY src ./src

# Gera o JAR
RUN ./gradlew bootJar --no-daemon

# -----------------------------

# Estágio 2: Runtime leve
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/build/libs/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Start da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]