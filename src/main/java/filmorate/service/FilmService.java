package filmorate.service;

import filmorate.exceptions.film.*;
import filmorate.exceptions.user.UserNotExist;
import filmorate.model.Film;
import filmorate.model.User;
import filmorate.storage.film.InMemoryFilmStorage;
import filmorate.storage.user.InMemoryUserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
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
        validate(film);
        Film newFilm = Film.builder()
                .id(getNextId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();

        return filmStorage.create(newFilm);
    }

    public Film update(Film film) {
        if (filmStorage.getFilms().contains(film)) {
            validate(film);
            if (film.getLikes() == null) {
                film.setLikes(filmStorage.getFilm(film.getId()).getLikes());
            }
            filmStorage.update(film);
            return film;
        }
        log.warn("Фильим не найден.");
        throw new FilmNotExist("Фильм не найден.");
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new FilmNotExist("Фильм не найден!");
        }

        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new UserNotExist("Пользователь с id " + userId + " не найден!");
        }

        if (film.getLikes().contains(userId)) {
            log.warn("Нельзя поставить лайк больше одного раза.");
            throw new FilmAlreadyLikedByUser("Пользователь уже ставил лайк этому фильму!");
        }

        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new FilmNotExist("Фильм не найден!");
        }

        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new UserNotExist("Пользователь с id " + userId + " не найден!");
        }

        if (!film.getLikes().contains(userId)) {
            log.warn("Пользователь не оценивал этот фильм.");
            throw new FilmNotLikedByUser("Пользователь не ставил лайк этому фильму!");
        }

        filmStorage.deleteLike(film, userId);
        return film;
    }

    public Collection<Film> getTop10Films(Integer count) {
        log.info(filmStorage.getFilms().toString());
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }


    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма не может быть пустым.");
            throw new NullFilmNameException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.warn("Длина описания фильма не может быть больше 200 символов.");
            throw new FilmDescriptionTooLongException("Длина описания фильма не может быть больше 200 символов.");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза обязательна, не может быть раньше 28 декабря 1895 года.");
            throw new InvalidReleaseDateException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() == null || film.getDuration() < 0) {
            log.warn("Продолжительность фильма обязательна, должна быть положительным числом.");
            throw new InvalidDurationException("Продолжительность фильма должна быть положительным числом.");
        }
    }

    private long getNextId() {
        long currentMaxId = filmStorage.getFilms()
                .stream()
                .mapToLong(Film::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
