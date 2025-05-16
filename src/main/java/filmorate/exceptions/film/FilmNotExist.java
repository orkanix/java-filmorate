package filmorate.exceptions.film;

public class FilmNotExist extends RuntimeException {
    public FilmNotExist(String message) {
        super(message);
    }
}
