# Usa una imagen base de Amazon Corretto JDK 17
FROM amazoncorretto:17

# Instalar tar y otras dependencias necesarias para Maven
RUN yum install -y tar gzip

# Establece el directorio de trabajo en /home/app/
WORKDIR /home/app/

# Copia solo los archivos de configuración y dependencias
COPY ./mvnw ./mvnw
COPY ./.mvn/ ./.mvn
COPY ./pom.xml ./pom.xml

# Descarga las dependencias y compila
RUN ./mvnw dependency:go-offline

# Luego copia el código fuente
COPY ./src ./src

# Compila el código (sin volver a descargar dependencias)
RUN ./mvnw clean package -DskipTests

# Configura las opciones de memoria para Java
ENV JAVA_TOOL_OPTIONS="-Xmx8192m"

# Define el comando de entrada para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dconfig.file=./application.conf", "-Dspring.config.location=file:///home/app/application.properties", "-jar", "target/familiahuecas-0.0.1-SNAPSHOT.jar"]
