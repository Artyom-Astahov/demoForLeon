# Используйте официальный образ maven для сборки приложения
FROM openjdk:23 AS build
WORKDIR /app
# Скопируйте pom.xml и скачайте зависимости для кеширования
COPY Ulpom.xml .
RUN mvn dependency:go-offline

# Скопируйте исходные файлы и выполните сборку
COPY ../src /app/src
RUN mvn package

# Используйте официальный образ OpenJDK для запуска приложения
FROM openjdk:23
WORKDIR /app
# Копируем собранный jar-файл в образ
COPY --from=build /app/target/task-1.0.0.jar ./app.jar
# Запускаем приложение
CMD ["java", "-jar", "app.jar"]
