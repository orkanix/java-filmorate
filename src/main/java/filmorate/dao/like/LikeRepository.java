package filmorate.dao.like;

import filmorate.dao.BaseRepository;
import filmorate.model.Like;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class LikeRepository extends BaseRepository<Like> {
    private static final String FIND_FILMS_BY_FILM_ID_QUERY = "SELECT * FROM likes WHERE film_id = ?";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";

    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    public List<Like> getFilmLikes(long id) {
        return findMany(FIND_FILMS_BY_FILM_ID_QUERY, id);
    }

    public void addLike(long filmId, long userId) {
        jdbc.update(INSERT_LIKE_QUERY, filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        jdbc.update(DELETE_LIKE_QUERY, filmId, userId);
    }
}
