FROM openjdk:23-jdk AS build

WORKDIR /app

COPY pom.xml . 
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
RUN mvn dependency:resolve

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:23-jdk

WORKDIR /app

COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
