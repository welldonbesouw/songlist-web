#### Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine as build

EXPOSE 8080

# ENV JAVA_HOME=/opt/java/openjdk

# Set the current working directory inside the image
WORKDIR /app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Build all the dependencies in preparation to go offline
# This is a separate step so the dependencies will be cached unless the pom.xml file has changed
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


#### Stage 2: A minimal docker image with command to run the app
FROM eclipse-temurin:17-jdk-alpine

ARG DEPENDENCY=/app/target/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# USER 1001

#ENTRYPOINT ["java", "-cp","app:app/lib/*","com.unseensonglist.songlist.SonglistApplication"]
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -cp app:app/lib/* com.unseensonglist.songlist.SonglistApplication"]
