# java-filmorate
https://app.diagrams.net/#G1akw_GtVd2g-L7lai9J4CuxTk5UwARygZ#%7B%22pageId%22%3A%22rfk9L2AMqvFMQHHUSDV9%22%7D
Template repository for Filmorate project.
<img width="930" height="514" alt="Снимок экрана 2025-09-09 в 12 22 30" src="https://github.com/user-attachments/assets/1aa6886c-5bf8-45f5-9fa1-f0e226e6f9db" />

Ниже приведены примеры основных операций.

Пользователи (Users)

Создать пользователя: POST /users с данными: id, email, login, name, birthday.
Пример: email: user1@example.com, login: user1login, name: User One, birthday: 1990-01-01.

Получить пользователя по ID: GET /users/{id}.

Обновить пользователя: PUT /users с данными: id, email, login, name, birthday.
Пример: id: 1, email: user1_updated@example.com, login: user1login, name: User One Updated, birthday: 1990-01-01.

Получить всех пользователей: GET /users.

Друзья (Friends)

Добавить друга: PUT /users/{userId}/friends/{friendId}.

Удалить друга: DELETE /users/{userId}/friends/{friendId}.

Получить список друзей пользователя: GET /users/{userId}/friends.

Получить общих друзей: GET /users/{userId}/friends/common/{otherUserId}.

Фильмы (Films)

Создать фильм: POST /films с данными: name, description, releaseDate, duration, mpa, genres.
Пример: name: Inception, description: A mind-bending thriller, releaseDate: 2010-07-16, duration: 148, mpa: PG-13, genres: Action, Sci-Fi.

Обновить фильм: PUT /films с данными: id, name, description, releaseDate, duration, mpa, genres.
Пример: id: 1, name: Inception Updated, description: Updated description, releaseDate: 2010-07-16, duration: 148, mpa: PG-13, genres: Action.

Получить все фильмы: GET /films.

Получить популярные фильмы: GET /films/popular?count=10.

Поставить лайк фильму: PUT /films/{filmId}/like/{userId}.

Удалить лайк: DELETE /films/{filmId}/like/{userId}.
