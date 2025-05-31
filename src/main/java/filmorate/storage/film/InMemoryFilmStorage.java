package filmorate.storage.film;

import filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();

    public Film create(Film film) {
        films.put(film.getId(), film);
        log.info("Успешное создание фильма.");
        return film;
    }

    public Film update(Film film) {
        films.put(film.getId(), film);
        log.info("Успешное обновление фильма.");
        return film;
    }

    public void deleteLike(Film film, Long id) {
        film.getLikes().remove(id);
        log.info("Лайк успешно удален!");
    }

    public Collection<Film> getFilms() {
        log.info("Успешный вывод фильмов.");
        return films.values();
    }

    public Film getFilm(Long id) {
        log.info("Успешный вывод фильма.");
        return films.get(id);
    }
}
