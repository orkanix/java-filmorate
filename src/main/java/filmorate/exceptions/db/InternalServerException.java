package filmorate.exceptions.db;

public class InternalServerException extends RuntimeException {
  public InternalServerException(String message) {
    super(message);
  }
}