package filmorate.controllers;

import filmorate.exceptions.film.*;
import filmorate.exceptions.film.FilmNotExist;
import lombok.extern.slf4j.Slf4j;
import filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final HashMap<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Успешный вывод фильмов.");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        validate(film);
        Film newFilm = Film.builder()
                .id(getNextId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();

        log.info("Успешное создание фильма.");
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (films.containsValue(film)) {
            validate(film);
            films.put(film.getId(), film);
            log.info("Успешное обновление фильма.");
            return film;
        }
        log.warn("Фильим не найден.");
        throw new FilmNotExist("Фильм не найден.");
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
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
