package filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    @Builder.Default
    private Set<Long> likes = new HashSet<>();
}
