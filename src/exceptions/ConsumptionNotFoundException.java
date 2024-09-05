package exceptions;

public class ConsumptionNotFoundException extends RuntimeException {
    public ConsumptionNotFoundException(String message) {
        super(message);
    }
}
