package filmorate.dto.film;

import filmorate.model.Genre;
import filmorate.model.Rating;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewFilmRequest {
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Rating mpa;
    List<Genre> genres;
}
