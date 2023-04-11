package tips.zadanie2.tips_zadanie2.exceptions;

public class IOManagerWriteException extends IOManagerGeneralException {
    public IOManagerWriteException(String message) {
        super(message);
    }

    public IOManagerWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
