package filmorate.controllers;

import filmorate.exceptions.user.*;
import lombok.extern.slf4j.Slf4j;
import filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Успешный вывод пользователей.");
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
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

    @PutMapping
    public User updateUser(@RequestBody User user) {
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
