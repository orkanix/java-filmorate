package filmorate.storage.user;

import filmorate.exceptions.user.InvalidBirthdayException;
import filmorate.exceptions.user.InvalidEmailException;
import filmorate.exceptions.user.InvalidLoginException;
import filmorate.exceptions.user.UserNotExist;
import filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();

    public User create(User user) {
        validate(user);
        User newUser = User.builder()
                .id(getNextId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName() == null ? user.getLogin() : user.getName())
                .birthday(user.getBirthday())
                .build();

        log.info("Успешное создание пользователя.");
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User update(User user) {
        if (users.containsValue(user)) {
            validate(user);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Успешное обновление данных пользователя.");
            return user;
        }
        log.warn("Пользователь не найден.");
        throw new UserNotExist("Пользователь не найден.");
    }

    public void deleteFriend(User user, Long id) {
        user.getFriends().remove(id);
        log.info("Друг с id: " + id + "успешно удален.");
    }

    public Collection<User> getUsers() {
        log.info("Успешный вывод пользователей.");
        return users.values();
    }

    public User getUser(Long id) {
        return users.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotExist("Пользователь с id " + id + " не найден!"));
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
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
