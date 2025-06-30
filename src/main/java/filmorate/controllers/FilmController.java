package filmorate.controllers;

import filmorate.dto.film.FilmDto;
import filmorate.dto.film.NewFilmRequest;
import filmorate.dto.film.UpdateFilmRequest;
import filmorate.service.FilmService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<FilmDto> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")
    public FilmDto getFilmById(@PathVariable long filmId) {
        return filmService.getFilm(filmId);
    }


    @GetMapping("/popular")
    public Collection<FilmDto> getTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
    }

    @PostMapping
    public FilmDto createFilm(@RequestBody NewFilmRequest request) {
        return filmService.create(request);
    }

    @PutMapping()
    public FilmDto updateFilm(@RequestBody UpdateFilmRequest request) {
        return filmService.update(request);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmDto addLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmDto deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.deleteLike(id, userId);
    }
}
