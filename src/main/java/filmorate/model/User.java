package filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    @Builder.Default
    Set<User> friends = new HashSet<>();
}
