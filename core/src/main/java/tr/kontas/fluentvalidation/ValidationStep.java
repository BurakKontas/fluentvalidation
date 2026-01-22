package tr.kontas.fluentvalidation;

import java.util.function.Predicate;

class ValidationStep<R> {

    private final Predicate<R> predicate;
    private String message;

    public ValidationStep(Predicate<R> predicate, String message) {
        this.predicate = predicate;
        this.message = message;
    }

    boolean isValid(R value) {
        return predicate.test(value);
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }
}
