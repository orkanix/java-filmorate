package filmorate.service;

import filmorate.dao.film.FilmRepository;
import filmorate.dao.film.mappers.FilmMapper;
import filmorate.dao.user.UserRepository;
import filmorate.dto.film.FilmDto;
import filmorate.dto.film.NewFilmRequest;
import filmorate.dto.film.UpdateFilmRequest;
import filmorate.exceptions.db.EntityNotFoundException;
import filmorate.exceptions.film.*;
import filmorate.model.Film;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    public FilmDto create(NewFilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);
        validate(film);
        film = filmRepository.create(film);

        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(UpdateFilmRequest request) {
        Film updatedFilm = filmRepository.getFilm(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, request))
                .orElseThrow(() -> new EntityNotFoundException("Фильм с id " + request.getId() + " не найден."));
        validate(updatedFilm);

        updatedFilm = filmRepository.update(updatedFilm)
                .orElseThrow(() -> new EntityNotFoundException("Фильм не найден."));

        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public Collection<FilmDto> getFilms() {
        return filmRepository.getFilms()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public FilmDto getFilm(long filmId) {
        Film film = filmRepository.getFilm(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Фильм не найден!"));
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto addLike(Long filmId, Long userId) {
        userRepository.getUser(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден!"));

        Film film = filmRepository.getFilm(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Фильм не найден!"));

        boolean alreadyLiked = film.getLikes().stream()
                .anyMatch(like -> like.getUserId().equals(userId));

        if (alreadyLiked) {
            log.warn("Нельзя поставить лайк больше одного раза.");
            throw new FilmAlreadyLikedByUser("Пользователь уже ставил лайк этому фильму!");
        }

        Film updatedFilm = filmRepository.addLike(filmId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Фильм с id " + filmId + " не найден."));

        return FilmMapper.mapToFilmDto(updatedFilm);
    }


    public FilmDto deleteLike(Long filmId, Long userId) {
        userRepository.getUser(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден!"));

        Film film = filmRepository.removeLike(filmId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Фильм не найден!"));

        return FilmMapper.mapToFilmDto(film);
    }

    public Collection<FilmDto> getTop10Films(Integer count) {
        return filmRepository.getTopFilms(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toCollection(ArrayList::new));
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

}