package filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class User {

    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @Builder.Default
    private Set<Long> friends = new HashSet<>();
}
