# java-filmorate
Template repository for Filmorate project.
<img width="417" height="378" alt="Снимок экрана 2025-09-09 в 22 43 38" src="https://github.com/user-attachments/assets/0f61eb2b-4d33-4584-8d9d-424f8106ee65" />


-- Добавляем жанр

INSERT INTO genre (name) VALUES ('Комедия');

-- Добавляем MPA рейтинг

INSERT INTO mpa (name) VALUES ('PG-13');

-- Добавляем фильм

INSERT INTO films (name, description, duration, genre_id, mpa_id, release_date)

VALUES ('Интерстеллар', 'Фильм о космосе и путешествиях', 169, 1, 1, '2014-11-07');

-- Добавляем пользователя

INSERT INTO users (login, name, email, birthday)

VALUES ('astrofan', 'Космический фанат', 'astro@mail.com', '1990-03-14');

-- Пользователь ставит лайк фильму

INSERT INTO film_likes (user_id, film_id)

VALUES (1, 1);

-- Добавляем дружбу между пользователями

INSERT INTO friendship (user_id, friend_id)

VALUES (1, 2);
