package filmorate.dto.film;

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
public class UpdateFilmRequest {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Rating mpa;
    List<Genre> genres = new ArrayList<>();
    List<Like> likes = new ArrayList<>();

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return ! (description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return ! (releaseDate == null);
    }

    public boolean hasDuration() {
        return ! (releaseDate == null);
    }

    public boolean hasMpa() {
        return ! (mpa == null);
    }

    public boolean hasGenres() {
        return ! (genres.isEmpty());
    }

    public boolean hasLikes() {
        return ! (likes.isEmpty());
    }
}
