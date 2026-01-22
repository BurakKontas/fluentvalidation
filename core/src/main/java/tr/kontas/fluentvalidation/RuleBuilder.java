package tr.kontas.fluentvalidation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RuleBuilder<T, R> {

    private final Function<T, R> propertyAccessor;
    private final String propertyName;
    private final List<ValidationStep<R>> steps = new ArrayList<>();

    private CascadeMode cascadeMode = CascadeMode.CONTINUE;
    private ValidationStep<R> lastStep;

    public RuleBuilder(String propertyName, Function<T, R> propertyAccessor) {
        this.propertyName = propertyName;
        this.propertyAccessor = propertyAccessor;
    }

    public RuleBuilder<T, R> cascade(CascadeMode mode) {
        this.cascadeMode = mode;
        return this;
    }

    public RuleBuilder<T, R> must(Predicate<R> predicate, String message) {
        ValidationStep<R> step = new ValidationStep<>(predicate, message);
        steps.add(step);
        lastStep = step;
        return this;
    }

    public RuleBuilder<T, R> withMessage(String message) {
        if (lastStep == null) {
            throw new IllegalStateException(
                    "withMessage() must be called after a validation rule"
            );
        }
        lastStep.setMessage(message);
        return this;
    }

    void validate(T target, ValidationResult result) {
        R value = propertyAccessor.apply(target);

        for (ValidationStep<R> step : steps) {
            if (!step.isValid(value)) {

                result.addError(
                        propertyName,
                        step.getMessage()
                );

                if (cascadeMode == CascadeMode.STOP) {
                    return;
                }
            }
        }
    }

    public RuleBuilder<T, R> notNull() {
        return must(v -> v != null, "must not be null");
    }

    public RuleBuilder<T, R> notEmpty() {
        return must(
                v -> v != null && !v.toString().isEmpty(),
                "must not be empty"
        );
    }

    public RuleBuilder<T, R> notBlank() {
        return must(
                v -> v != null && !v.toString().trim().isEmpty(),
                "must not be blank"
        );
    }

    public RuleBuilder<T, R> equalTo(R other) {
        return must(
                v -> v != null && v.equals(other),
                "must be equal to " + other
        );
    }

    public RuleBuilder<T, R> greaterThan(Number min) {
        return must(
                v -> v instanceof Number
                        && ((Number) v).doubleValue() > min.doubleValue(),
                "must be greater than " + min
        );
    }

    public RuleBuilder<T, R> lessThan(Number max) {
        return must(
                v -> v instanceof Number
                        && ((Number) v).doubleValue() < max.doubleValue(),
                "must be less than " + max
        );
    }

    public RuleBuilder<T, R> minLength(int length) {
        return must(
                v -> v != null && v.toString().length() >= length,
                "must be at least " + length + " characters long"
        );
    }

    public RuleBuilder<T, R> email() {
        return must(
                v -> v != null && EMAIL_PATTERN.matcher(v.toString()).matches(),
                "must be a valid email address"
        );
    }

    public RuleBuilder<T, R> matches(String regex) {
        return matches(Pattern.compile(regex));
    }

    public RuleBuilder<T, R> matches(Pattern pattern) {
        return must(
                v -> v != null && pattern.matcher(v.toString()).matches(),
                "must match pattern: " + pattern.pattern()
        );
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
}
