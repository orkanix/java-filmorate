package filmorate.exceptions.user;

public class UserNotExist extends RuntimeException {
    public UserNotExist(String message) {
        super(message);
    }
}
