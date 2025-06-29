package filmorate.dao.film;

import filmorate.dao.BaseRepository;
import filmorate.dao.genre.GenreRepository;
import filmorate.dao.like.LikeRepository;
import filmorate.dao.rating.RatingRepository;
import filmorate.exceptions.db.EntityNotFoundException;
import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.model.Like;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;


@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> {
    private final RatingRepository ratingRepository;
    private final GenreRepository genresRepository;
    private final LikeRepository likeRepository;
    private static final String FIND_ALL_QUERY = "SELECT * FROM films;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?;";
    private static final String FIND_TOP_10 = "SELECT f.film_id, f.name, f.description, f.releaseDate, f.duration, f.rating_id, COUNT(l.user_id) AS likes_count FROM films f LEFT JOIN likes l ON f.film_id = l.film_id GROUP BY f.film_id, f.name, f.description, f.releaseDate, f.duration, f.rating_id ORDER BY likes_count DESC LIMIT ?;";
    private static final String INSERT_QUERY = "INSERT INTO films (name, description, releaseDate, duration, rating_id) " +
            "VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE films " +
            "SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? " +
            "WHERE film_id = ?;";


    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, RatingRepository ratingRepository, GenreRepository genresRepository, LikeRepository likeRepository) {
        super(jdbc, mapper);
        this.ratingRepository = ratingRepository;
        this.genresRepository = genresRepository;
        this.likeRepository = likeRepository;
    }

    public Optional<Film> getFilm(long id) {
        Optional<Film> filmOptional = findOne(FIND_BY_ID_QUERY, id);
        if (filmOptional.isPresent()) {
            Film film = filmOptional.get();

            List<Genre> genres = genresRepository.getFilmGenres(film.getId());
            film.setGenres(genres);

            List<Like> likes = likeRepository.getFilmLikes(film.getId());
            film.setLikes(likes);

            return Optional.of(film);
        }
        return Optional.empty();
    }

    public List<Film> getFilms() {
        return findMany(FIND_ALL_QUERY).stream()
                .peek(film -> film.setGenres(genresRepository.getFilmGenres(film.getId())))
                .peek(film -> film.setLikes(likeRepository.getFilmLikes(film.getId())))
                .toList();
    }

    public List<Film> getTop10Films(Integer limit) {
        return jdbc.query(FIND_TOP_10, mapper, limit);
    }

    public Film create(Film film) {
        long mpaId = film.getMpa().getId();

        if (!ratingRepository.ratingExists(mpaId)) {
            throw new EntityNotFoundException("Рейтинг с id " + mpaId + " не существует");
        }

        if (film.getGenres() != null) {
            boolean allGenresExist = film.getGenres().stream()
                    .allMatch(genre -> genresRepository.getGenre(genre.getId()).isPresent());

            if (!allGenresExist) {
                throw new EntityNotFoundException("Один или несколько жанров не существуют");
            }
        }

        long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        ratingRepository.getRating(film.getMpa().getId());
        genresRepository.create(film.getId(), film.getGenres());

        return film;
    }

    public Film update(Film film) {
        update(UPDATE_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    public Optional<Film> addLike(long filmId, long userId) {
        likeRepository.addLike(filmId, userId);
        return getFilm(filmId);
    }

    public Optional<Film> removeLike(long filmId, long userId) {
        boolean alreadyLiked = getFilm(filmId).stream().anyMatch(film -> film.getLikes().stream()
                .anyMatch(like -> like.getUserId().equals(userId)));
        if (!alreadyLiked) {
            return Optional.empty();
        }
        likeRepository.removeLike(filmId, userId);
        return getFilm(filmId);
    }
}
