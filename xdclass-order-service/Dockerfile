FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


#每一个docker都会有jdk 存在jdk过大都问题


#JAR_FILE 是打包后的地址，在maven中配置
#<buildArgs>
#   <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
#</buildArgs>
# <finalName>alibaba-cloud-api-gateway</finalName>