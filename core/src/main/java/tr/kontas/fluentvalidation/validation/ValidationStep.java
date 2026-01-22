package tr.kontas.fluentvalidation.validation;

import java.util.function.Predicate;

public class ValidationStep<R> {

    private final Predicate<R> predicate;
    private String message;

    public ValidationStep(Predicate<R> predicate, String message) {
        this.predicate = predicate;
        this.message = message;
    }

    public boolean isValid(R value) {
        return predicate.test(value);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
