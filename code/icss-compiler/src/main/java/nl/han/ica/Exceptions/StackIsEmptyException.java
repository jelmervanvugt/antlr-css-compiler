package nl.han.ica.Exceptions;

public class StackIsEmptyException extends RuntimeException {
    public StackIsEmptyException(String err) {
        super(err);
    }
}
