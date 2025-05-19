# Sử dụng JDK 17 làm base image
FROM openjdk:17-jdk-slim

LABEL authors="tuanv"

# Tạo thư mục trong container để chứa file
WORKDIR /app

# Copy file JAR vào container
COPY target/apiwebNews-0.0.1-SNAPSHOT.jar app.jar

# Cấu hình port (khớp với port trong application.properties)
EXPOSE 8080

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
