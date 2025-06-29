package filmorate.dao.genre;

import filmorate.dao.BaseRepository;
import filmorate.model.Genre;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres;";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT g.genre_id, g.name " +
                    "FROM films_genres fg " +
                    "JOIN genres g ON fg.genre_id = g.genre_id " +
                    "WHERE fg.film_id = ? " +
                    "ORDER BY g.genre_id;";
    private static final String INSERT_QUERY = "MERGE INTO films_genres (film_id, genre_id) KEY (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM films_genres WHERE film_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Genre> getGenre(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Genre> getGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    public List<Genre> getFilmGenres(long filmId) {
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }

    public void create(long filmId, List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            delete(filmId);
            return;
        }
        for (Genre genre : genres) {
            update(INSERT_QUERY, filmId, genre.getId());
        }
    }

    public void delete(long id) {
        update(DELETE_QUERY, id);
    }

}
