package filmorate.service;

import filmorate.dao.user.UserRepository;
import filmorate.dao.user.mappers.UserMapper;
import filmorate.dto.user.NewUserRequest;
import filmorate.dto.user.UpdateUserRequest;
import filmorate.dto.user.UserDto;
import filmorate.exceptions.user.*;
import filmorate.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto create(NewUserRequest request) {
        User user = UserMapper.mapToUser(request);

        validate(user);

        user = userRepository.create(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UpdateUserRequest request) {
        User updatedUser = userRepository.getUser(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new UserNotExist("Пользователя не существует."));

        validate(updatedUser);
        if (updatedUser.getName() == null || updatedUser.getName().isBlank()) {
            updatedUser.setName(updatedUser.getLogin());
        }

        updatedUser = userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public Collection<UserDto> getUsers() {
        return userRepository.getUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<UserDto> getFriends(Long id) {
        User user = userRepository.getUser(id)
                .orElseThrow(() -> new UserNotExist("Пользователь с id " + id + " не найден!"));

        return userRepository.getFriends(user).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getMutualFriends(Long firstUserId, Long secondUserId) {
        userRepository.getUser(firstUserId)
                .orElseThrow(() -> new UserNotExist("Пользователь с id " + firstUserId + " не найден!"));
        userRepository.getUser(secondUserId)
                .orElseThrow(() -> new UserNotExist("Пользователь с id " + secondUserId + " не найден!"));

        return userRepository.getCommonFriends(firstUserId, secondUserId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto addFriend(Long userId, Long friendId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new UserNotExist("Пользователь с id " + userId + " не найден!"));

        userRepository.getUser(friendId)
                .orElseThrow(() -> new UserNotExist("Пользователь с id " + friendId + " не найден!"));


        user = userRepository.addFriend(user, friendId)
                .orElseThrow(() -> new UserNotExist("Пользователь с id " + userId + " не найден!"));

        return UserMapper.mapToUserDto(user);
    }


    public UserDto deleteFriend(Long userId, Long friendId) {
        userRepository.getUser(userId)
                .orElseThrow(() -> new UserNotExist("Пользователь с id " + userId + " не найден!"));

        userRepository.getUser(friendId)
                .orElseThrow(() -> new UserNotExist("Пользователь с id " + friendId + " не найден!"));

        userRepository.deleteFriend(userId, friendId);

        User updatedUser = userRepository.getUser(userId)
                .orElseThrow(() -> new UserNotExist("Пользователь с id " + userId + " не найден после удаления друга!"));

        return UserMapper.mapToUserDto(updatedUser);
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

}