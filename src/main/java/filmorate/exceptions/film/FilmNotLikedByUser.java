package filmorate.exceptions.film;

public class FilmNotLikedByUser extends RuntimeException {
    public FilmNotLikedByUser(String message) {
        super(message);
    }
}
