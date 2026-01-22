package tr.kontas.fluentvalidation;

public class FluentValidationException extends RuntimeException {

    private final ValidationResult result;

    public FluentValidationException(ValidationResult result) {
        super(buildMessage(result));
        this.result = result;
    }

    public ValidationResult getResult() {
        return result;
    }

    @Override
    public String getMessage() {
        return buildMessage(result);
    }

    @Override
    public String toString() {
        return buildMessage(result);
    }

    private static String buildMessage(ValidationResult result) {
        return result.toString();
    }
}
