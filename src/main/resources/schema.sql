CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email varchar(60) NOT NULL UNIQUE,
  login varchar(30) NOT NULL UNIQUE,
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
  CONSTRAINT pk_friendships PRIMARY KEY (requester_id, addressee_id),
  CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES users (user_id) ON DELETE CASCADE,
  CONSTRAINT fk_addressee FOREIGN KEY (addressee_id) REFERENCES users (user_id) ON DELETE CASCADE
);