FROM openjdk:17
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar
ENV FILEPATH=/app/output/scrapedData.csv
CMD ["java","-jar","app.jar"]