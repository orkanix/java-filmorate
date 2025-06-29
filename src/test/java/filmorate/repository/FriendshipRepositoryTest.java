package filmorate.repository;

import filmorate.ApplicationStarter;
import filmorate.dao.friendship.FriendshipRepository;
import filmorate.dao.user.UserRepository;
import filmorate.dao.user.mappers.UserMapper;
import filmorate.dto.user.NewUserRequest;
import filmorate.model.Friendship;
import filmorate.model.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FriendshipRepository.class})
@SpringBootTest(classes = ApplicationStarter.class)
public class FriendshipRepositoryTest {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

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
        friendshipRepository.addFriendship(user1.getId(), user3.getId());
    }

    @Test
    public void getFriendshipsTest() {
        List<Friendship> friendships = friendshipRepository.getFriendships(user1.getId());

        assertThat(friendships)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .anySatisfy(friendship ->
                        assertThat(friendship).hasFieldOrPropertyWithValue("friendId", user3.getId())
                );
    }

    @Test
    public void getFriendIdsTest() {
        List<Long> ids = friendshipRepository.getFriendIds(user1.getId());

        assertThat(ids)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(user3.getId());
    }

    @Test
    public void addFriendshipTest() {
        friendshipRepository.addFriendship(user1.getId(), user2.getId());

        assertThat(friendshipRepository.getFriendships(user1.getId()))
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .anySatisfy(friendship ->
                        assertThat(friendship).hasFieldOrPropertyWithValue("friendId", user2.getId())
                )
                .anySatisfy(friendship ->
                        assertThat(friendship).hasFieldOrPropertyWithValue("friendId", user3.getId())
                );
    }

    @Test
    public void removeFriendshipTest() {
        friendshipRepository.addFriendship(user1.getId(), user2.getId());

        assertThat(friendshipRepository.getFriendships(user1.getId()))
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .anySatisfy(friendship ->
                        assertThat(friendship).hasFieldOrPropertyWithValue("friendId", user2.getId())
                )
                .anySatisfy(friendship ->
                        assertThat(friendship).hasFieldOrPropertyWithValue("friendId", user3.getId())
                );

        friendshipRepository.removeFriendship(user1.getId(), user2.getId());

        assertThat(friendshipRepository.getFriendships(user1.getId()))
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .anySatisfy(friendship ->
                        assertThat(friendship).hasFieldOrPropertyWithValue("friendId", user3.getId())
                );
    }
}
