package filmorate.exceptions.film;

public class FilmDescriptionTooLongException extends RuntimeException {
    public FilmDescriptionTooLongException(String message) {
        super(message);
    }
}
