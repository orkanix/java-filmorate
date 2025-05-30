package filmorate.service;

import filmorate.exceptions.user.*;
import filmorate.model.User;
import filmorate.storage.user.InMemoryUserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        validate(user);
        User newUser = User.builder()
                .id(getNextId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName() == null ? user.getLogin() : user.getName())
                .birthday(user.getBirthday())
                .build();
        return userStorage.create(newUser);
    }

    public User update(User user) {
        if (userStorage.getUsers().contains(user)) {
            validate(user);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            userStorage.update(user);
            return user;
        }
        log.warn("Пользователь не найден.");
        throw new UserNotExist("Пользователь не найден.");
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public List<User> getFriends(Long id) {
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new UserNotExist("Пользователь с id " + id + " не найден!");
        }

        return user.getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(Long firstUserId, Long secondUserId) {
        User firstUser = userStorage.getUser(firstUserId);
        if (firstUser == null) {
            throw new UserNotExist("Пользователь с id " + firstUserId + " не найден!");
        }
        User secondUser = userStorage.getUser(secondUserId);
        if (secondUser == null) {
            throw new UserNotExist("Пользователь с id " + secondUserId + " не найден!");
        }

        Set<Long> mutualFriendIds = firstUser.getFriends();
        mutualFriendIds.retainAll(secondUser.getFriends());

        return mutualFriendIds.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new UserNotExist("Пользователь с id " + userId + " не найден!");
        }
        User friend = userStorage.getUser(friendId);
        if (friend == null) {
            throw new UserNotExist("Пользователь с id " + friendId + " не найден!");
        }

        if (user.getFriends().contains(friendId)) {
            throw new AlreadyContainsInFriends("У пользователя уже есть друг с id: " + friendId);
        }

        user.getFriends().add(friendId);
        return user;
    }

    public User deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new UserNotExist("Пользователь с id " + userId + " не найден!");
        }
        User friend = userStorage.getUser(friendId);
        if (friend == null) {
            throw new UserNotExist("Пользователь с id " + friendId + " не найден!");
        }

        if (!user.getFriends().contains(friendId)) {
            throw new NotContainsInFriends("У пользователя нет друга с id: " + friendId);
        }

        userStorage.deleteFriend(user, friendId);
        return friend;
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Электронная почта не может быть пустой и должна содержать символ @.");
            throw new InvalidEmailException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Логин не может быть пустым и содержать пробелы.");
            throw new InvalidLoginException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть пустой или в будущем.");
            throw new InvalidBirthdayException("Дата рождения не может быть в будущем");
        }
    }

    private long getNextId() {
        long currentMaxId = userStorage.getUsers()
                .stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
