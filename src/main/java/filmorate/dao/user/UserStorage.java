package filmorate.dao.user;

import filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    User update(User user);

    void deleteFriend(Long userId, Long friendId);

    Collection<User> getUsers();

    Optional<User> getUser(Long id);
}
