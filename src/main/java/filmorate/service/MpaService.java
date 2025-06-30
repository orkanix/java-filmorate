package filmorate.service;

import filmorate.dao.rating.RatingRepository;
import filmorate.exceptions.db.EntityNotFoundException;
import filmorate.model.Rating;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaService {
    private final RatingRepository ratingRepository;

    public Rating getMpa(long id) {
        return ratingRepository.getRating(id)
                .orElseThrow(() -> new EntityNotFoundException("Рейтинг не найден."));
    }

    public List<Rating> getRatings() {
        return ratingRepository.getRatings();
    }

}
