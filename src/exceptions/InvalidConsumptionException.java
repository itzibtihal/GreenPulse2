package exceptions;

public class InvalidConsumptionException extends RuntimeException {
    public InvalidConsumptionException(String message) {
        super(message);
    }
}