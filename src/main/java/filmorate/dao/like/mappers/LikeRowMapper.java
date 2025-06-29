package filmorate.dao.like.mappers;

import filmorate.model.Like;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        Like like = new Like();
        like.setFilmId(rs.getLong("film_id"));
        like.setUserId(rs.getLong("user_id"));

        return like;
    }
}
