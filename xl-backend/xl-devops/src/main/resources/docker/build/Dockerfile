# 使用JDK 17作为基础镜像
FROM openjdk:17-jdk
#FROM openjdk:17-jdk-alpine
# 复制构建的JAR包到容器
ARG JAR_FILE=/xl-backend/xl-*/target/xl-*.jar
COPY ${JAR_FILE} /user/local/app.jar
WORKDIR /user/local/
# 运行应用
ENTRYPOINT ["java", "-jar", "./app.jar"]