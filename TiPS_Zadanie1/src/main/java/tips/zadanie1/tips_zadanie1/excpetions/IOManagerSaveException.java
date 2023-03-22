package tips.zadanie1.tips_zadanie1.excpetions;

public class IOManagerSaveException extends RuntimeException {
    public IOManagerSaveException(String message) {
        super(message);
    }

    public IOManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
