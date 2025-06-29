package filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "filmId")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Like {
    Long filmId;
    Long userId;
}
