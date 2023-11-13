FROM eclipse-temurin:17-jdk-ubi9-minimal

WORKDIR /app

COPY target/*.jar songlist-app.jar

EXPOSE 8080

USER 1001

CMD ["java", "-jar", "songlist-app.jar"]