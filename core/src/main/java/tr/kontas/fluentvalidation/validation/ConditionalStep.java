package tr.kontas.fluentvalidation.validation;

import java.util.function.Predicate;

public class ConditionalStep<T, R> {
    private final Predicate<T> entityCondition;
    private final boolean shouldValidate;
    private final Predicate<R> valuePredicate;
    private String message;

    public ConditionalStep(Predicate<T> entityCondition, boolean shouldValidate,
                           Predicate<R> valuePredicate, String message) {
        this.entityCondition = entityCondition;
        this.shouldValidate = shouldValidate;
        this.valuePredicate = valuePredicate;
        this.message = message;
    }

    public boolean shouldExecute(T entity) {
        boolean conditionMet = entityCondition.test(entity);
        return shouldValidate == conditionMet;
    }

    public boolean isValid(R value) {
        return valuePredicate.test(value);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}