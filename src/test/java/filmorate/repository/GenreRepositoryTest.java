package filmorate.repository;

import filmorate.ApplicationStarter;
import filmorate.dao.film.FilmRepository;
import filmorate.dao.film.mappers.FilmMapper;
import filmorate.dao.genre.GenreRepository;
import filmorate.dto.film.NewFilmRequest;
import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.model.Rating;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreRepository.class})
@SpringBootTest(classes = ApplicationStarter.class)
public class GenreRepositoryTest {
    private final GenreRepository genreRepository;
    private final FilmRepository filmRepository;

    @Test
    public void getGenreTest() {
        Optional<Genre> genreOptional =
                genreRepository.getGenre(1L);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(otherGenre ->
                        assertThat(otherGenre).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    public void getGenresTest() {
        List<Genre> genres = genreRepository.getGenres();

        assertThat(genres)
                .isNotNull()
                .hasSize(6)
                .anySatisfy(otherGenre ->
                        assertThat(otherGenre).hasFieldOrPropertyWithValue("name", "Комедия")
                )
                .anySatisfy(otherGenre ->
                        assertThat(otherGenre).hasFieldOrPropertyWithValue("name", "Драма")
                );
    }

    @Test
    public void getFilmGenresTest() {
        NewFilmRequest newFilm = new NewFilmRequest("film", "film description", LocalDate.of(1990, 10, 15), 100, new Rating(1L, "P"), new ArrayList<>(List.of(new Genre(1L, "Комедия"))));
        Film film = filmRepository.create(FilmMapper.mapToFilm(newFilm));

        List<Genre> genres = genreRepository.getFilmGenres(film.getId());

        assertThat(genres)
                .isNotNull()
                .hasSize(1)
                .anySatisfy(otherGenre ->
                        assertThat(otherGenre).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    public void createTest() {
        NewFilmRequest newFilm = new NewFilmRequest("film", "film description", LocalDate.of(1990, 10, 15), 100, new Rating(1L, "P"), new ArrayList<>(List.of(new Genre(1L, "Комедия"))));
        Film film = filmRepository.create(FilmMapper.mapToFilm(newFilm));

        List<Genre> newGenre = new ArrayList<>();
        newGenre.add(new Genre(2L, "Драма"));

        genreRepository.create(film.getId(), newGenre);

        assertThat(genreRepository.getFilmGenres(film.getId()))
                .isNotNull()
                .hasSize(2)
                .anySatisfy(otherGenre ->
                        assertThat(otherGenre).hasFieldOrPropertyWithValue("name", "Комедия")
                )
                .anySatisfy(otherGenre ->
                        assertThat(otherGenre).hasFieldOrPropertyWithValue("name", "Драма")
                );
    }

    @Test
    public void deleteTest() {
        NewFilmRequest newFilm = new NewFilmRequest("film", "film description", LocalDate.of(1990, 10, 15), 100, new Rating(1L, "P"), new ArrayList<>(List.of(new Genre(1L, "Комедия"))));
        Film film = filmRepository.create(FilmMapper.mapToFilm(newFilm));

        List<Genre> newGenre = new ArrayList<>();
        newGenre.add(new Genre(2L, "Драма"));

        genreRepository.create(film.getId(), newGenre);

        assertThat(genreRepository.getFilmGenres(film.getId()))
                .isNotNull()
                .hasSize(2)
                .anySatisfy(otherGenre ->
                        assertThat(otherGenre).hasFieldOrPropertyWithValue("name", "Комедия")
                )
                .anySatisfy(otherGenre ->
                        assertThat(otherGenre).hasFieldOrPropertyWithValue("name", "Драма")
                );

        genreRepository.delete(film.getId());

        assertThat(genreRepository.getFilmGenres(film.getId()))
                .isEmpty();
    }
}
