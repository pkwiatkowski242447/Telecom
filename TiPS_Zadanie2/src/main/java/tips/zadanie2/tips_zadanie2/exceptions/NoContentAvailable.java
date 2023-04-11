package tips.zadanie2.tips_zadanie2.exceptions;

public class NoContentAvailable extends IllegalArgumentException {
    public NoContentAvailable(String s) {
        super(s);
    }

    public NoContentAvailable(String message, Throwable cause) {
        super(message, cause);
    }
}
