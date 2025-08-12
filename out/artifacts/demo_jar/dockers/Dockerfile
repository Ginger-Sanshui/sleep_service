FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY E:/music_server/out/artifacts/demo_jar/dockers/demo.jar ./demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]
EXPOSE 8084