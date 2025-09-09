INSERT INTO users(email, login, name, birthday) VALUES
('alice@example.com','alice','Alice Johnson','1995-04-12'),
('bob@example.com','bobby','Bob Smith','1990-07-23'),
('carol@example.com','carol','Carol White','1988-12-05');

INSERT INTO friends(user_id, friend_id, status) VALUES
(1,2,'CONFIRMED'),
(2,1,'CONFIRMED'),
(2,3,'UNCONFIRMED');

INSERT INTO friend_requests(user_id, friend_id) VALUES
(1,3);

INSERT INTO mpa(name) VALUES
('G'),('PG'),('PG-13'),('R'),('NC-17');

INSERT INTO genres(name) VALUES
('Comedy'),('Drama'),('Action'),('Horror'),('Sci-Fi');

INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES
('Inception','A mind-bending thriller','2010-07-16',148,3),
('The Matrix','Virtual reality action','1999-03-31',136,4),
('The Godfather','Mafia family drama','1972-03-24',175,4),
('Toy Story','Animated adventure','1995-11-22',81,1);

INSERT INTO film_genres(film_id, genre_id) VALUES
(1,5),(2,5),(3,2),(4,1);

INSERT INTO films_likes(film_id, user_id) VALUES
(1,1),(1,2),(2,2),(3,3),(4,1);