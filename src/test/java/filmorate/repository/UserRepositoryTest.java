package filmorate.repository;

import filmorate.ApplicationStarter;
import filmorate.dao.user.UserRepository;
import filmorate.dao.user.mappers.UserMapper;
import filmorate.dto.user.NewUserRequest;
import filmorate.model.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class})
@SpringBootTest(classes = ApplicationStarter.class)
public class UserRepositoryTest {
    private final UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void beforeEach() {
        NewUserRequest newUser1 = new NewUserRequest("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1));
        NewUserRequest newUser2 = new NewUserRequest("user2@example.com", "user2", "User Two", LocalDate.of(2000, 4, 9));
        NewUserRequest newUser3 = new NewUserRequest("user3@example.com", "user3", "User Three", LocalDate.of(1950, 9, 16));
        user1 = userRepository.create(UserMapper.mapToUser(newUser1));
        user2 = userRepository.create(UserMapper.mapToUser(newUser2));
        user3 = userRepository.create(UserMapper.mapToUser(newUser3));
    }

    @Test
    public void getUserTest() {
        Optional<User> userOptional = 
                userRepository.getUser(user1.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(otherUser ->
                        assertThat(otherUser).hasFieldOrPropertyWithValue("login", "user1")
                );
    }

    @Test
    public void getUsersTest() {
        Collection<User> userCollection = userRepository.getUsers();

        assertThat(userCollection)
                .isNotNull()
                .isNotEmpty()
                .hasSize(userRepository.getUsers().size())
                .anySatisfy(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "user1@example.com")
                );
    }

    @Test
    public void createUserTest() {
        NewUserRequest request = new NewUserRequest("new@example.com", "newlogin", "New Name", LocalDate.of(1995, 6, 15));
        User newUser = UserMapper.mapToUser(request);

        User createdUser = userRepository.create(newUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isPositive();
        assertThat(createdUser.getEmail()).isEqualTo("new@example.com");
        assertThat(createdUser.getLogin()).isEqualTo("newlogin");
        assertThat(createdUser.getName()).isEqualTo("New Name");
        assertThat(createdUser.getBirthday()).isEqualTo(LocalDate.of(1995, 6, 15));

        Optional<User> savedUser = userRepository.getUser(createdUser.getId());
        assertThat(savedUser).isPresent();
    }

    @Test
    public void updateUserTest() {
        user1.setName("Updated Name");
        user1.setLogin("updatedlogin");
        user1.setEmail("updated@example.com");

        User updatedUser = userRepository.update(user1);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(user1.getId());
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getLogin()).isEqualTo("updatedlogin");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");

        Optional<User> userFromDb = userRepository.getUser(user1.getId());
        assertThat(userFromDb).isPresent();
        assertThat(userFromDb.get().getName()).isEqualTo("Updated Name");
    }

    @Test
    public void addFriendTest() {
        Optional<User> result = userRepository.addFriend(user1, user2.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(user1.getId());

        Collection<User> friends = userRepository.getFriends(user1);

        assertThat(friends)
                .isNotEmpty()
                .hasSize(1)
                .extracting(User::getId)
                .contains(user2.getId());
    }

    @Test
    public void getFriendsTest() {
        userRepository.addFriend(user1, user2.getId());
        userRepository.addFriend(user1, user3.getId());

        List<User> friends = userRepository.getFriends(user1);

        assertThat(friends)
                .isNotNull()
                .hasSize(2)
                .extracting(User::getId)
                .containsExactlyInAnyOrder(user2.getId(), user3.getId());
    }

    @Test
    public void deleteFriendTest() {
        userRepository.addFriend(user1, user2.getId());

        List<User> friendsBefore = userRepository.getFriends(user1);
        assertThat(friendsBefore)
                .extracting(User::getId)
                .contains(user2.getId());

        userRepository.deleteFriend(user1.getId(), user2.getId());

        List<User> friendsAfter = userRepository.getFriends(user1);
        assertThat(friendsAfter)
                .extracting(User::getId)
                .doesNotContain(user2.getId());
    }

    @Test
    public void getCommonFriendsTest() {
        userRepository.addFriend(user1, user2.getId());
        userRepository.addFriend(user1, user3.getId());

        userRepository.addFriend(user2, user3.getId());
        userRepository.addFriend(user2, user1.getId());

        List<User> commonFriends = userRepository.getCommonFriends(user1.getId(), user2.getId());

        assertThat(commonFriends)
                .isNotNull()
                .hasSize(1)
                .extracting(User::getId)
                .containsExactly(user3.getId());
    }


}
