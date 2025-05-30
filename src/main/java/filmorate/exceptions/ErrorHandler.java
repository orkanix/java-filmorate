package filmorate.exceptions;

import filmorate.exceptions.film.*;
import filmorate.exceptions.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({UserNotExist.class, FilmNotExist.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({
            InvalidBirthdayException.class,
            InvalidEmailException.class,
            FilmDescriptionTooLongException.class,
            InvalidDurationException.class,
            InvalidReleaseDateException.class,
            NullFilmNameException.class,
            InvalidLoginException.class,
            AlreadyContainsInFriends.class,
            FilmAlreadyLikedByUser.class,
            FilmNotLikedByUser.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestExceptions(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ErrorResponse handlerNotContainsInFriends(final NotContainsInFriends e) {
        return new ErrorResponse(e.getMessage());
    }
}
