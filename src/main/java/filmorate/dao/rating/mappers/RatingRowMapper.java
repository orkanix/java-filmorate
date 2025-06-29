package filmorate.dao.rating.mappers;

import filmorate.model.Rating;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingRowMapper implements RowMapper<Rating> {
    @Override
    public Rating mapRow(ResultSet rs, int rowNum) throws SQLException {
        Rating rating = new Rating();
        rating.setId(rs.getLong("rating_id"));
        rating.setName(rs.getString("name"));

        return rating;
    }
}
