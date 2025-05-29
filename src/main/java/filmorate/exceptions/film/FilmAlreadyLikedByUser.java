package filmorate.exceptions.film;

public class FilmAlreadyLikedByUser extends RuntimeException {
    public FilmAlreadyLikedByUser(String message) {
        super(message);
    }
}
