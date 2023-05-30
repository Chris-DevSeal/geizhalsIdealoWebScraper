FROM openjdk:17
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar
COPY ./src/main/resources/output/scrapedData.csv /app/output/scrapedData.csv
CMD ["java","-jar","app.jar"]