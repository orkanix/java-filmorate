package filmorate.dao.rating;

import filmorate.dao.BaseRepository;
import filmorate.exceptions.db.EntityNotFoundException;
import filmorate.model.Rating;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingRepository extends BaseRepository<Rating> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE rating_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings ORDER BY rating_id;";
    private static final String CHECK_EXISTS_QUERY = "SELECT COUNT(*) FROM ratings WHERE rating_id = ?";

    public RatingRepository(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Rating> getRating(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Rating> getRatings() {
        return findMany(FIND_ALL_QUERY);
    }

    public boolean ratingExists(long mpaId) {
        Integer count = jdbc.queryForObject(CHECK_EXISTS_QUERY, Integer.class, mpaId);
        return count > 0;
    }

}
