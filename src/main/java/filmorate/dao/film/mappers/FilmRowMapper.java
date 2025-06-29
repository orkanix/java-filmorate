package filmorate.dao.film.mappers;

import filmorate.dao.rating.RatingRepository;
import filmorate.model.Film;
import filmorate.model.Rating;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    private final RatingRepository ratingRepository;

    public FilmRowMapper(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        Long ratingId = rs.getLong("rating_id");
        Optional<Rating> rating = ratingRepository.getRating(ratingId);
        film.setMpa(rating.get());

        return film;
    }
}
