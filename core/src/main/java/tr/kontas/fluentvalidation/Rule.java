package tr.kontas.fluentvalidation;

import java.util.function.Function;
import java.util.function.Predicate;

public class Rule<T, R> {

    private final Function<T, R> property;
    private Predicate<R> condition;
    private String message;

    public Rule(Function<T, R> property) {
        this.property = property;
    }

    public Rule<T, R> must(Predicate<R> condition) {
        this.condition = condition;
        return this;
    }

    public Rule<T, R> withMessage(String message) {
        this.message = message;
        return this;
    }

    public void validate(T target, ValidationResult result) {
        R value = property.apply(target);
        if (!condition.test(value)) {
            result.addError(message);
        }
    }
}
