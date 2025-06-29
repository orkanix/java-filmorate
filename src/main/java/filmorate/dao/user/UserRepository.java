package filmorate.dao.user;

import filmorate.dao.BaseRepository;
import filmorate.dao.friendship.FriendshipRepository;
import filmorate.model.Friendship;
import filmorate.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            List<Friendship> friends = friendshipRepository.getFriendships(user.getId());
            user.setFriends(new HashSet<>(friends));
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public List<User> getFriends(User user) {
        return friendshipRepository.getFriendships(user.getId()).stream()
                .map(friendship -> getUser(friendship.getFriendId()))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public Optional<User> addFriend(User user, Long friendId) {
        friendshipRepository.addFriendship(user.getId(), friendId);
        return getUser(user.getId());
    }

    public List<User> getCommonFriends(long userId1, long userId2) {
        List<Long> friendsOfUser1 = friendshipRepository.getFriendIds(userId1);
        List<Long> friendsOfUser2 = friendshipRepository.getFriendIds(userId2);

        friendsOfUser1.retainAll(friendsOfUser2);

        if (friendsOfUser1.isEmpty()) {
            return List.of();
        }

        String sql = String.format("SELECT * FROM users WHERE user_id IN (%s)",
                friendsOfUser1.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));

        return findMany(sql);
    }

}
