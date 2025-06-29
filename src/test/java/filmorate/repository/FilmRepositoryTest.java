package filmorate.repository;

import filmorate.ApplicationStarter;
import filmorate.dao.film.FilmRepository;
import filmorate.dao.film.mappers.FilmMapper;
import filmorate.dao.like.LikeRepository;
import filmorate.dao.user.UserRepository;
import filmorate.dao.user.mappers.UserMapper;
import filmorate.dto.film.NewFilmRequest;
import filmorate.dto.user.NewUserRequest;
import filmorate.model.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class})
@SpringBootTest(classes = ApplicationStarter.class)
public class FilmRepositoryTest {
    private final FilmRepository filmRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    private Film film1;

    @BeforeEach
    public void beforeEach() {
        NewFilmRequest newFilm1 = new NewFilmRequest("film1", "film description", LocalDate.of(1990, 10, 15), 100, new Rating(1L, "P"), new ArrayList<>(List.of(new Genre(1L, "Комедия"))));
        NewFilmRequest newFilm2 = new NewFilmRequest("film2", "film description", LocalDate.of(2000, 10, 15), 100, new Rating(2L, "PG"), new ArrayList<>(List.of(new Genre(1L, "Комедия"))));
        NewFilmRequest newFilm3 = new NewFilmRequest("film3", "film description", LocalDate.of(1960, 10, 15), 100, new Rating(1L, "P"), new ArrayList<>(List.of(new Genre(2L, "Драма"))));
        film1 = filmRepository.create(FilmMapper.mapToFilm(newFilm1));
        filmRepository.create(FilmMapper.mapToFilm(newFilm2));
        filmRepository.create(FilmMapper.mapToFilm(newFilm3));
    }

    @Test
    public void getFilmTest() {
        Optional<Film> filmOptional = filmRepository.getFilm(film1.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(otherFilm ->
                        assertThat(otherFilm).hasFieldOrPropertyWithValue("name", "film1")
                );
    }

    @Test
    public void getFilmsTest() {
        List<Film> films = filmRepository.getFilms();

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(6)
                .anySatisfy(otherFilm ->
                        assertThat(otherFilm).hasFieldOrPropertyWithValue("name", "film1")
                )
                .anySatisfy(otherFilm ->
                        assertThat(otherFilm).hasFieldOrPropertyWithValue("name", "film3")
                );
    }

    @Test
    public void getTop10FilmsTest() {
        List<Film> films = filmRepository.getFilms();

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .anySatisfy(otherFilm ->
                        assertThat(otherFilm).hasFieldOrPropertyWithValue("name", "film1")
                )
                .anySatisfy(otherFilm ->
                        assertThat(otherFilm).hasFieldOrPropertyWithValue("name", "film3")
                );
    }

    @Test
    public void createFilmTest() {
        NewFilmRequest request = new NewFilmRequest("newFilm", "film description", LocalDate.of(1990, 10, 15), 100, new Rating(1L, "P"), new ArrayList<>(List.of(new Genre(1L, "Комедия"))));
        Film newFilm = FilmMapper.mapToFilm(request);

        Film createdFilm = filmRepository.create(newFilm);

        assertThat(createdFilm).isNotNull();
        assertThat(createdFilm.getId()).isPositive();
        assertThat(createdFilm.getName()).isEqualTo("newFilm");
        assertThat(createdFilm.getDuration()).isEqualTo(100);
        assertThat(createdFilm.getReleaseDate()).isEqualTo(LocalDate.of(1990, 10, 15));

        Optional<Film> savedFilm = filmRepository.getFilm(newFilm.getId());
        assertThat(savedFilm).isPresent();
    }

    @Test
    public void updateFilmTest() {
        film1.setName("Updated Name");
        film1.setDuration(150);
        film1.setReleaseDate(LocalDate.of(2010, 10, 15));

        Film updatedFilm = filmRepository.update(film1);

        assertThat(updatedFilm).isNotNull();
        assertThat(updatedFilm.getId()).isEqualTo(film1.getId());
        assertThat(updatedFilm.getName()).isEqualTo("Updated Name");
        assertThat(updatedFilm.getDuration()).isEqualTo(150);
        assertThat(updatedFilm.getReleaseDate()).isEqualTo(LocalDate.of(2010, 10, 15));

        Optional<Film> userFromDb = filmRepository.getFilm(film1.getId());
        assertThat(userFromDb).isPresent();
        assertThat(userFromDb.get().getName()).isEqualTo("Updated Name");
    }

    @Test
    public void addLikeTest() {
        NewUserRequest newUser1 = new NewUserRequest("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1));
        NewUserRequest newUser2 = new NewUserRequest("user2@example.com", "user2", "User Two", LocalDate.of(2000, 4, 9));
        User user1 = userRepository.create(UserMapper.mapToUser(newUser1));
        User user2 = userRepository.create(UserMapper.mapToUser(newUser2));

        filmRepository.addLike(film1.getId(), user1.getId());
        Optional<Film> likedFilm = filmRepository.addLike(film1.getId(), user2.getId());

        assertThat(likedFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes())
                                .extracting(like -> Objects.equals(like.getUserId(), user1.getId()))
                );
    }

    @Test
    public void removeLikeTest() {
        NewUserRequest newUser1 = new NewUserRequest("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1));
        NewUserRequest newUser2 = new NewUserRequest("user2@example.com", "user2", "User Two", LocalDate.of(2000, 4, 9));
        User user1 = userRepository.create(UserMapper.mapToUser(newUser1));
        User user2 = userRepository.create(UserMapper.mapToUser(newUser2));

        filmRepository.addLike(film1.getId(), user1.getId());
        Optional<Film> likedFilm = filmRepository.addLike(film1.getId(), user2.getId());

        assertThat(likedFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes())
                                .extracting(Like::getUserId)
                                .contains(user2.getId())
                );

        likedFilm = filmRepository.removeLike(film1.getId(), user2.getId());

        assertThat(likedFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes())
                                .extracting(Like::getUserId)
                                .doesNotContain(user2.getId())
                                .contains(user1.getId())
                );
    }

}
