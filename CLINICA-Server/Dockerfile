# Pasul 1: Build (Folosim Maven cu Java 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copiem pom.xml și descărcăm dependențele
COPY pom.xml .
RUN mvn dependency:go-offline
# Copiem codul sursă și compilăm totul într-un singur JAR (Fat JAR)
COPY src ./src
RUN mvn clean package -DskipTests

# Pasul 2: Runtime (Imagine ușoară de Java)
FROM eclipse-temurin:21-jre
WORKDIR /app
# Atenție: verifică dacă în target fișierul se numește așa. Dacă nu, pune *.jar
COPY --from=build /app/target/*.jar app.jar
EXPOSE 1234
CMD ["java", "-jar", "app.jar"]