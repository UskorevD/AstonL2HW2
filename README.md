# Задание 4
## Обязательно к выполнению:
* Spring
* Добавить в user-service поддержку Spring и разработать API, которое позволит управлять данными.
* Использовать необходимые модули spring(boot, web, data etc).
* Реализовать api для получения, создания, обновления и удаления юзера. Важно, entity не должен возвращаться из контроллера, необходимо использовать dto.
* Заменить Hibernate на Spring data JPA.
* Написать тесты для API(можно делать это при помощи mockMvc или других средств)

## Технологии
* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Validation
* PostgreSQL
* Maven
* JUnit
* MockMvc

## Выполнено
* Spring Boot приложение.
* REST API для создания, получения, обновления и удаления пользователей.
* Spring Data JPA для работы с базой данных.
* DTO для запросов и ответов.
* Валидация входных данных.
* Обработка ошибок и исключений.
* Тестирование API с использованием MockMvc.

## Структура проекта
* controller — REST API для обработки HTTP-запросов.
* dto — объекты запросов и ответов API.
* entity — сущности базы данных.
* exception — исключения и обработка ошибок.
* repository — доступ к данным через Spring Data JPA.
* service — бизнес-логика приложения.
* resources — конфигурация приложения.
* test — тестирование REST API с использованием MockMvc.

## Чтобы запустить и проверить

1. Установите PostgreSQL и создайте базу данных `user_service`.
2. В src\main\resources создайте файл `application.properties` cо следующим содержимым:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/user_service
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```
3. Замените USERNAME и PASSWORD на свои имя пользователя и пароль.
4. Не забудьте добавить `application.properties` в .gitignore и убедитесь, что файл не попадает в Git.
