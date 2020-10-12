package nl.han.ica.icss.checker.errors;

public class VariableUndefinedException extends RuntimeException {
    public VariableUndefinedException(String errorMessage) {
        super(errorMessage);
    }
}
