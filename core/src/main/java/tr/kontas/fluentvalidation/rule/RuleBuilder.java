package tr.kontas.fluentvalidation.rule;

import tr.kontas.fluentvalidation.dtos.PasswordMessages;
import tr.kontas.fluentvalidation.enums.CascadeMode;
import tr.kontas.fluentvalidation.validation.ValidationResult;
import tr.kontas.fluentvalidation.validation.ValidationStep;

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

    public void validate(T target, ValidationResult result) {
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

    // rules

    public RuleBuilder<T, R> notNull() {
        return notNull("must not be null");
    }

    public RuleBuilder<T, R> notNull(String message) {
        return must(v -> v != null, message);
    }

    public RuleBuilder<T, R> notEmpty() {
        return notEmpty("must not be empty");
    }

    public RuleBuilder<T, R> notEmpty(String message) {
        return must(
                v -> v != null && !v.toString().isEmpty(),
                message
        );
    }

    public RuleBuilder<T, R> notBlank() {
        return notBlank("must not be blank");
    }

    public RuleBuilder<T, R> notBlank(String message) {
        return must(
                v -> v != null && !v.toString().trim().isEmpty(),
                message
        );
    }

    public RuleBuilder<T, R> equalTo(R other) {
        return equalTo(other, "must be equal to " + other);
    }

    public RuleBuilder<T, R> equalTo(R other, String message) {
        return must(
                v -> v != null && v.equals(other),
                message
        );
    }

    public RuleBuilder<T, R> greaterThan(Number min) {
        return greaterThan(min, "must be greater than " + min);
    }

    public RuleBuilder<T, R> greaterThan(Number min, String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() > min.doubleValue(),
                message
        );
    }

    public RuleBuilder<T, R> lessThan(Number max) {
        return lessThan(max, "must be less than " + max);
    }

    public RuleBuilder<T, R> lessThan(Number max, String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() < max.doubleValue(),
                message
        );
    }

    public RuleBuilder<T, R> minLength(int length) {
        return minLength(length, "must be at least " + length + " characters long");
    }

    public RuleBuilder<T, R> minLength(int length, String message) {
        return must(
                v -> v != null && v.toString().length() >= length,
                message
        );
    }

    public RuleBuilder<T, R> maxLength(int length) {
        return minLength(length, "must be at maximum " + length + " characters long");
    }

    public RuleBuilder<T, R> maxLength(int length, String message) {
        return must(
                v -> v != null && v.toString().length() <= length,
                message
        );
    }

    public RuleBuilder<T, R> email() {
        return email("must be a valid email address");
    }

    public RuleBuilder<T, R> email(String message) {
        return must(
                v -> v != null && EMAIL_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    public RuleBuilder<T, R> containsUppercase() {
        return containsUppercase("must contain at least one uppercase letter");
    }

    public RuleBuilder<T, R> containsUppercase(String message) {
        return must(
                v -> v != null && v.toString().chars().anyMatch(Character::isUpperCase),
                message
        );
    }

    public RuleBuilder<T, R> containsLowercase() {
        return containsLowercase("must contain at least one lowercase letter");
    }

    public RuleBuilder<T, R> containsLowercase(String message) {
        return must(
                v -> v != null && v.toString().chars().anyMatch(Character::isLowerCase),
                message
        );
    }

    public RuleBuilder<T, R> containsDigit() {
        return containsDigit("must contain at least one digit");
    }

    public RuleBuilder<T, R> containsDigit(String message) {
        return must(
                v -> v != null && v.toString().chars().anyMatch(Character::isDigit),
                message
        );
    }

    public RuleBuilder<T, R> containsSpecialChar() {
        return containsSpecialChar("must contain at least one special character");
    }

    public RuleBuilder<T, R> containsSpecialChar(String message) {
        return must(
                v -> v != null && v.toString().chars()
                        .anyMatch(ch -> !Character.isLetterOrDigit(ch)),
                message
        );
    }

    public RuleBuilder<T, R> strongPassword(Number minLength, Number maxLength) {
        return strongPassword(
                minLength,
                maxLength,
                PasswordMessages.defaults()
        );
    }

    public RuleBuilder<T, R> strongPassword(Number minLength, Number maxLength, PasswordMessages messages) {
        return this
                .minLength(minLength.intValue(), messages.minLength(minLength.intValue()))
                .maxLength(maxLength.intValue(), messages.maxLength(maxLength.intValue()))
                .containsUppercase(messages.uppercase())
                .containsLowercase(messages.lowercase())
                .containsDigit(messages.digit())
                .containsSpecialChar(messages.specialChar());
    }

    public RuleBuilder<T, R> matches(String regex) {
        return matches(regex, "must match pattern: " + regex);
    }

    public RuleBuilder<T, R> matches(String regex, String message) {
        return matches(Pattern.compile(regex), message);
    }

    public RuleBuilder<T, R> matches(Pattern pattern) {
        return matches(pattern, "must match pattern: " + pattern.pattern());
    }

    public RuleBuilder<T, R> matches(Pattern pattern, String message) {
        return must(
                v -> v != null && pattern.matcher(v.toString()).matches(),
                message
        );
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
}
