package filmorate.service;

import filmorate.dao.genre.GenreRepository;
import filmorate.exceptions.db.EntityNotFoundException;
import filmorate.model.Genre;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre getGenre(long id) {
        return genreRepository.getGenre(id)
                .orElseThrow(() -> new EntityNotFoundException("Жанр с id " + id + " не найден."));
    }

    public List<Genre> getGenres() {
        return genreRepository.getGenres();
    }
}
