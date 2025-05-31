package filmorate.storage.user;

import filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);
    User update(User user);
    void deleteFriend(User user, Long id);
    Collection<User> getUsers();
    User getUser(Long id);
}
