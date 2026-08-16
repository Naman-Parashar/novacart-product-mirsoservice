FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/product_service-0.0.1-SNAPSHOT.jar product-service.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","product-service.jar"]