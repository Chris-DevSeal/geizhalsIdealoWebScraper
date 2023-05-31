FROM openjdk:17
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar
ENV FILEPATH=/output/scrapedData.csv
COPY ./src/main/resources/output/scrapedData.csv /app/output/scrapedData.csv
CMD ["java","-jar","app.jar","--filePath=app/scrapedData.csv"]