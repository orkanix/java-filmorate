package filmorate.dao.friendship;

import filmorate.dao.BaseRepository;
import filmorate.model.Friendship;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FriendshipRepository extends BaseRepository<Friendship> {
    private static final String FIND_MANY_QUERY = "SELECT * FROM friendships WHERE requester_id = ?";
    private static final String INSERT_NEW_FRIEND =
            "INSERT INTO friendships (requester_id, addressee_id) VALUES (?, ?)";
    private static final String CHECK_FRIENDSHIP_EXISTENCE =
            "SELECT COUNT(*) FROM friendships WHERE requester_id = ? AND addressee_id = ?";
    private static final String GET_FRIEND_IDS =
            "SELECT addressee_id FROM friendships WHERE requester_id = ?";
    private static final String DELETE_FRIENDSHIP =
            "DELETE FROM friendships WHERE requester_id = ? AND addressee_id = ?";

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);
    }

    public List<Friendship> getFriendships(long id) {
        return findMany(FIND_MANY_QUERY, id);
    }

    public List<Long> getFriendIds(long userId) {
        return jdbc.queryForList(GET_FRIEND_IDS, Long.class, userId);
    }

    public void addFriendship(Long requesterId, Long addresseeId) {
        Integer existing = jdbc.queryForObject(
                CHECK_FRIENDSHIP_EXISTENCE,
                Integer.class,
                requesterId, addresseeId
        );

        if (existing != null && existing > 0) {
            return;
        }

        update(INSERT_NEW_FRIEND, requesterId, addresseeId);
    }

    public void removeFriendship(Long requesterId, Long addresseeId) {
        Integer count = jdbc.queryForObject(
                CHECK_FRIENDSHIP_EXISTENCE,
                Integer.class, requesterId, addresseeId
        );

        if (count == null || count == 0) {
            return;
        }

        update(DELETE_FRIENDSHIP, requesterId, addresseeId);
    }


}
