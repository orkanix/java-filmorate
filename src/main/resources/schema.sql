CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email varchar(60) NOT NULL,
  login varchar(30) NOT NULL,
  name varchar(20),
  birthday date
);

CREATE TABLE IF NOT EXISTS ratings (
  rating_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name varchar(5) UNIQUE
);

CREATE TABLE IF NOT EXISTS genres (
  genre_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name varchar(30) UNIQUE
);

CREATE TABLE IF NOT EXISTS films (
  film_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name varchar(30) NOT NULL,
  description varchar(200) NOT NULL,
  releaseDate date NOT NULL,
  duration integer NOT NULL,
  rating_id BIGINT NOT NULL,
  CONSTRAINT fk_rating FOREIGN KEY (rating_id) REFERENCES ratings(rating_id)
);

CREATE TABLE IF NOT EXISTS films_genres (
  film_id BIGINT,
  genre_id BIGINT,
  CONSTRAINT pk_films_genres PRIMARY KEY (film_id, genre_id),
  CONSTRAINT fk_film FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE,
  CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
  film_id BIGINT,
  user_id BIGINT,
  CONSTRAINT pk_likes PRIMARY KEY (film_id, user_id),
  CONSTRAINT fk_film_likes FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE,
  CONSTRAINT fk_user_likes FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendships (
  requester_id BIGINT,
  addressee_id BIGINT,
  status varchar(10),
  CONSTRAINT pk_friendships PRIMARY KEY (requester_id, addressee_id),
  CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES users (user_id) ON DELETE CASCADE,
  CONSTRAINT fk_addressee FOREIGN KEY (addressee_id) REFERENCES users (user_id) ON DELETE CASCADE
);

INSERT INTO ratings (name)
SELECT 'G'
WHERE NOT EXISTS (SELECT 1 FROM ratings WHERE name = 'G');

INSERT INTO ratings (name)
SELECT 'PG'
WHERE NOT EXISTS (SELECT 1 FROM ratings WHERE name = 'PG');

INSERT INTO ratings (name)
SELECT 'PG-13'
WHERE NOT EXISTS (SELECT 1 FROM ratings WHERE name = 'PG-13');

INSERT INTO ratings (name)
SELECT 'R'
WHERE NOT EXISTS (SELECT 1 FROM ratings WHERE name = 'R');

INSERT INTO ratings (name)
SELECT 'NC-17'
WHERE NOT EXISTS (SELECT 1 FROM ratings WHERE name = 'NC-17');


INSERT INTO genres (name)
SELECT 'Комедия'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Комедия');

INSERT INTO genres (name)
SELECT 'Драма'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Драма');

INSERT INTO genres (name)
SELECT 'Мультфильм'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Мультфильм');

INSERT INTO genres (name)
SELECT 'Триллер'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Триллер');

INSERT INTO genres (name)
SELECT 'Документальный'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Документальный');

INSERT INTO genres (name)
SELECT 'Боевик'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Боевик');