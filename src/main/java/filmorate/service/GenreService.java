package filmorate.service;

import filmorate.dao.genre.GenreRepository;
import filmorate.exceptions.db.EntityNotFoundException;
import filmorate.model.Genre;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public Genre getGenre(long id) {
        return genreRepository.getGenre(id)
                .orElseThrow(() -> new EntityNotFoundException("Жанр с id " + id + " не найден."));
    }

    public List<Genre> getGenres() {
        return genreRepository.getGenres();
    }
}
