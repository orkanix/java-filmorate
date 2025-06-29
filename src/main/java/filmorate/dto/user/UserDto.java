package filmorate.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmorate.model.Friendship;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    Set<Friendship> friends = new HashSet<>();
}
