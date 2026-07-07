# # Estágio de Build
# FROM maven:3.9.6-eclipse-temurin-21 AS build
# COPY . .
# RUN mvn clean package -DskipTests
#
# # Estágio de Execução
# FROM eclipse-temurin:21-jdk-jammy
# COPY --from=build /target/*.jar app.jar
# EXPOSE 6060
# ENTRYPOINT ["java", "-jar", "app.jar"]

# Etapa 1: build (roda o Maven só aqui, não vai pra imagem final)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: runtime — imagem final bem mais leve, só com o JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# O Koyeb injeta a porta que sua API deve escutar via variável PORT
ENV PORT=8080
EXPOSE 8080

# -Xmx limita o heap da JVM pra não estourar a RAM do plano free (geralmente ~512MB)
ENTRYPOINT ["sh", "-c", "java -Xmx350m -Xss256k -jar app.jar --server.port=${PORT}"]