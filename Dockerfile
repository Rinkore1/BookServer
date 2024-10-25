# 使用 OpenJDK 基础镜像构建 Spring Boot 应用
FROM openjdk:17-jdk

# 设置工作目录
WORKDIR /app

# 复制 build 文件夹中的 JAR 文件到容器中
COPY build/libs/BookServer.jar app.jar

# 运行 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar"]
