package filmorate.repository;

import filmorate.ApplicationStarter;
import filmorate.dao.rating.RatingRepository;
import filmorate.model.Rating;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({RatingRepository.class})
@SpringBootTest(classes = ApplicationStarter.class)
public class MpaRepositoryTest {
    private final RatingRepository ratingRepository;

    @Test
    public void getRatingTest() {
        Optional<Rating> ratingOptional = ratingRepository.getRating(1L);

        assertThat(ratingOptional)
                .isPresent()
                .hasValueSatisfying(otherRating ->
                        assertThat(otherRating).hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    public void getRatingsTest() {
        List<Rating> ratingOptional = ratingRepository.getRatings();

        assertThat(ratingOptional)
                .isNotEmpty()
                .hasSize(5)
                .anySatisfy(otherRating ->
                        assertThat(otherRating).hasFieldOrPropertyWithValue("name", "G")
                )
                .anySatisfy(otherRating ->
                        assertThat(otherRating).hasFieldOrPropertyWithValue("name", "PG")
                );
    }

    @Test
    public void ratingExistsTest() {
        assertFalse(ratingRepository.ratingExists(10L));
    }
}
