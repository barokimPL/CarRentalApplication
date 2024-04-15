package pl.sda.carrental.exception;

public class CannotBecomeManagerException extends Exception {
    public CannotBecomeManagerException(String customMessage) {
        super(customMessage);
    }
}
