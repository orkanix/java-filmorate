package filmorate.storage.user;

import filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();

    public User create(User user) {
        users.put(user.getId(), user);
        log.info("Успешное создание пользователя.");
        return user;
    }

    public User update(User user) {

        users.put(user.getId(), user);
        log.info("Успешное обновление данных пользователя.");
        return user;
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
        return users.get(id);
    }
}
