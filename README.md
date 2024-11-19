# Система управления задачами
- Код используемой БД с созданием таблиц расположен в файле **tables.sql**
- Описание API расположено по адресу http://localhost:8080/swagger-ui/index.html
## Для локального запуска проекта
- Создать БД как в docker-compose.yml файле
- Создать .jar файл проекта
- При необходимости поменять название и путь к .jar-файлу в Dockerfile
- Проверить соответствие свойств БД тому, что указано в окружении приложения в docker-compose файле
- Собрать Docker-контейнер и запустить
# Текст задания
## Разработка Системы Управления Задачами
### Описание задачи:
Вам необходимо разработать простую систему управления задачами (Task Management System) с использованием Java, Spring. 
Система должна обеспечивать создание, редактирование, удаление и просмотр задач. Каждая задача должна содержать заголовок, описание, статус (например, "в ожидании", "в процессе", "завершено"), приоритет (например, "высокий", "средний", "низкий") и комментарии, а также автора задачи и исполнителя. 
Реализовать необходимо только API.
### Требования:
1.	Сервис должен поддерживать аутентификацию и авторизацию пользователей по email и паролю.
2.	Доступ к API должен быть аутентифицирован с помощью JWT токена.
3.	Создать ролевую систему администратора и пользователей.
4.	Администратор может управлять всеми задачами: создавать новые, редактировать существующие, просматривать и удалять, менять статус и приоритет, назначать исполнителей задачи, оставлять комментарии.
5.	Пользователи могут управлять своими задачами, если указаны как исполнитель: менять статус, оставлять комментарии.
6.	API должно позволять получать задачи конкретного автора или исполнителя, а также все комментарии к ним. Необходимо обеспечить фильтрацию и пагинацию вывода.
7.	Сервис должен корректно обрабатывать ошибки и возвращать понятные сообщения, а также валидировать входящие данные.
8.	Сервис должен быть хорошо задокументирован. API должен быть описан с помощью Open API и Swagger. В сервисе должен быть настроен Swagger UI. Необходимо написать README с инструкциями для локального запуска проекта. Дев среду нужно поднимать с помощью docker compose.
9.	Напишите несколько базовых тестов для проверки основных функций вашей системы.
10.	Используйте для реализации системы язык Java 17+, Spring, Spring Boot. В качестве БД можно использовать PostgreSQL или MySQL. Для реализации аутентификации и авторизации нужно использовать Spring Security. Можно использовать дополнительные инструменты, если в этом есть необходимость (например кэш).
