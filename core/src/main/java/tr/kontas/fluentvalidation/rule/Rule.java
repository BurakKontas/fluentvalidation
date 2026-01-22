package tr.kontas.fluentvalidation.rule;

import tr.kontas.fluentvalidation.validation.ValidationResult;

import java.util.function.Function;
import java.util.function.Predicate;

public class Rule<T, R> {

    private final String propertyName;
    private final Function<T, R> propertyAccessor;

    private Predicate<R> condition;
    private String message;

    public Rule(String propertyName, Function<T, R> propertyAccessor) {
        this.propertyName = propertyName;
        this.propertyAccessor = propertyAccessor;
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

        R value = propertyAccessor.apply(target);

        if (condition == null || condition.test(value)) {
            return;
        }

        result.addError(
                propertyName,
                message != null ? message : "Validation failed"
        );
    }
}
