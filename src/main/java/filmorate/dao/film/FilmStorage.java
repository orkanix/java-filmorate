package filmorate.dao.film;

import filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    void deleteLike(Film film, Long id);

    Collection<Film> getFilms();

    Film getFilm(Long id);
}
