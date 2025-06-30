package filmorate.dao.user;

import filmorate.dao.BaseRepository;
import filmorate.dao.friendship.FriendshipRepository;
import filmorate.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private final FriendshipRepository friendshipRepository;
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper, FriendshipRepository friendshipRepository) {
        super(jdbc, mapper);
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public User create(User user) {
        long id = insert(INSERT_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        user.setId(id);

        return user;
    }

    @Override
    public User update(User user) {
        update(UPDATE_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        friendshipRepository.removeFriendship(userId, friendId);
    }

    @Override
    public Collection<User> getUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<User> getUser(Long id) {
        Optional<User> userOptional = findOne(FIND_BY_ID_QUERY, id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<User> friends = friendshipRepository.getFriendships(user.getId());
            user.setFriends(new HashSet<>(friends));
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public List<User> getFriends(User user) {
        return friendshipRepository.getFriendships(user.getId());
    }

    public Optional<User> addFriend(User user, Long friendId) {
        friendshipRepository.addFriendship(user.getId(), friendId);
        return getUser(user.getId());
    }

    public List<User> getCommonFriends(long userId1, long userId2) {
        return friendshipRepository.getCommonFriends(userId1, userId2);
    }

}
