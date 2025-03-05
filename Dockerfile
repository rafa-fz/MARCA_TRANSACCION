FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY target/transaccion-v1.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]