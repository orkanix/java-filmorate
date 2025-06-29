package filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmorate.model.Genre;
import filmorate.model.Like;
import filmorate.model.Rating;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Rating mpa;
    List<Genre> genres = new ArrayList<>();
    List<Like> likes = new ArrayList<>();
}
