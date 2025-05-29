package filmorate.service;

import filmorate.exceptions.user.AlreadyContainsInFriends;
import filmorate.exceptions.user.NotContainsInFriends;
import filmorate.model.User;
import filmorate.storage.user.InMemoryUserStorage;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public List<User> getFriends(Long id) {
        User user = userStorage.getUser(id);

        return userStorage.getUsers().stream()
                .filter(u -> user.getFriends().contains(u.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(Long firstUserId, Long secondUserId) {
        User firstUser = userStorage.getUser(firstUserId);
        User secondUser = userStorage.getUser(secondUserId);

        Set<Long> mutualFriendIds = firstUser.getFriends();
        mutualFriendIds.retainAll(secondUser.getFriends());

        return userStorage.getUsers().stream()
                .filter(user -> mutualFriendIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        userStorage.getUser(friendId);

        if (user.getFriends().contains(friendId)) {
            throw new AlreadyContainsInFriends("У пользователя уже есть друг с id: " + friendId);
        }

        user.getFriends().add(friendId);
        return user;
    }

    public User deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        if (!user.getFriends().contains(friendId)) {
            throw new NotContainsInFriends("У пользователя нет друга с id: " + friendId);
        }

        userStorage.deleteFriend(user, friendId);
        return friend;
    }

}
