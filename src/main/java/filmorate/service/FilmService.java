package filmorate.service;

import filmorate.exceptions.film.FilmAlreadyLikedByUser;
import filmorate.exceptions.film.FilmNotLikedByUser;
import filmorate.model.Film;
import filmorate.storage.film.InMemoryFilmStorage;
import filmorate.storage.user.InMemoryUserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        userStorage.getUser(userId);

        if (film.getLikes().contains(userId)) {
            log.warn("Нельзя поставить лайк больше одного раза.");
            throw new FilmAlreadyLikedByUser("Пользователь уже ставил лайк этому фильму!");
        }

        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        userStorage.getUser(userId);

        if (!film.getLikes().contains(userId)) {
            log.warn("Пользователь не оценивал этот фильм.");
            throw new FilmNotLikedByUser("Пользователь не ставил лайк этому фильму!");
        }

        filmStorage.deleteLike(film, userId);
        return film;
    }

    public Collection<Film> getTop10Films(Integer count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes() == null ? 0 : film.getLikes().size())
                        .reversed())
                .limit(Math.min(count, filmStorage.getFilms().size()))
                .collect(Collectors.toList());
    }

}
