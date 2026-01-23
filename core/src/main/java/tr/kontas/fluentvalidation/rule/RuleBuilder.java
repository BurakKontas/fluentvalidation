package tr.kontas.fluentvalidation.rule;

import tr.kontas.fluentvalidation.dtos.PasswordMessages;
import tr.kontas.fluentvalidation.enums.CascadeMode;
import tr.kontas.fluentvalidation.validation.ValidationResult;
import tr.kontas.fluentvalidation.validation.ValidationStep;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * A comprehensive fluent validation builder that allows creating validation rules
 * for any type of property with a fluent, readable API.
 *
 * <p>This class provides over 100 validation methods covering common validation scenarios
 * including null checks, string validation, numeric ranges, collections, dates, patterns,
 * international standards, and security validations.</p>
 *
 * <h2>Basic Usage</h2>
 * <pre>
 * {@code
 * RuleBuilder<User, String> rule = new RuleBuilder<>("email", User::getEmail);
 * rule.notNull().notBlank().email();
 * }
 * </pre>
 *
 * <h2>Usage with Validator</h2>
 * <pre>
 * {@code
 * public class UserValidator extends Validator<User> {
 *     public UserValidator() {
 *         ruleFor(User::getEmail)
 *             .notNull()
 *             .notBlank()
 *             .email()
 *             .withMessage("Please enter a valid email address");
 *
 *         ruleFor(User::getAge)
 *             .greaterThan(18)
 *             .lessThan(100);
 *     }
 * }
 * }
 * </pre>
 *
 * <p>The RuleBuilder follows a fluent interface pattern where validation rules
 * can be chained together to create complex validation scenarios.</p>
 *
 * @param <T> The type of the object being validated
 * @param <R> The type of the property being validated
 *
 * @author Kontas
 * @version 1.0
 * @see tr.kontas.fluentvalidation.validation.Validator
 * @see tr.kontas.fluentvalidation.validation.ValidationResult
 */
public class RuleBuilder<T, R> {

    private final Function<T, R> propertyAccessor;
    private final String propertyName;
    private final List<ValidationStep<R>> steps = new ArrayList<>();

    private CascadeMode cascadeMode = CascadeMode.CONTINUE;
    private ValidationStep<R> lastStep;

    /**
     * Creates a new RuleBuilder for the specified property.
     *
     * @param propertyName The name of the property (used in error messages)
     * @param propertyAccessor Function to access the property value
     */
    public RuleBuilder(String propertyName, Function<T, R> propertyAccessor) {
        this.propertyName = propertyName;
        this.propertyAccessor = propertyAccessor;
    }

    /**
     * Sets the cascade mode for validation rules.
     * When set to STOP, validation stops after the first failed rule for this property.
     * When set to CONTINUE (default), all rules are evaluated and all errors are collected.
     *
     * @param mode The cascade mode to use
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> cascade(CascadeMode mode) {
        this.cascadeMode = mode;
        return this;
    }

    /**
     * Adds a custom validation rule with a custom message.
     * This method allows creating custom validation logic when built-in validators are insufficient.
     *
     * @param predicate The validation logic (returns true if valid)
     * @param message The error message to display if validation fails
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getAge)
     *     .must(age -> age != null && age >= 18, "Must be at least 18 years old");
     * }
     * </pre>
     */
    public RuleBuilder<T, R> must(Predicate<R> predicate, String message) {
        ValidationStep<R> step = new ValidationStep<>(predicate, message);
        steps.add(step);
        lastStep = step;
        return this;
    }

    /**
     * Overrides the default error message for the most recently added validation rule.
     * Must be called immediately after a validation rule.
     *
     * @param message The custom error message
     * @return This RuleBuilder for chaining
     * @throws IllegalStateException if called before any validation rule
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getEmail)
     *     .notNull()
     *     .withMessage("Email is required");
     * }
     * </pre>
     */
    public RuleBuilder<T, R> withMessage(String message) {
        if (lastStep == null) {
            throw new IllegalStateException(
                    "withMessage() must be called after a validation rule"
            );
        }
        lastStep.setMessage(message);
        return this;
    }

    /**
     * Executes all validation rules against the target object and adds errors to the result.
     * This method is typically called internally by the Validator class.
     *
     * @param target The object to validate
     * @param result The ValidationResult to collect errors
     */
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

    // ========== BASIC RULES ==========

    /**
     * Validates that the property value is not null.
     * Works with any type of property.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getName).notNull();
     * }
     * </pre>
     */
    public RuleBuilder<T, R> notNull() {
        return notNull("must not be null");
    }

    /**
     * Validates that the property value is not null with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> notNull(String message) {
        return must(v -> v != null, message);
    }

    /**
     * Validates that the property value is null.
     * Useful for conditional validations where a field should be empty.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getMiddleName).isNull();
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isNull() {
        return isNull("must be null");
    }

    /**
     * Validates that the property value is null with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isNull(String message) {
        return must(v -> v == null, message);
    }

    /**
     * Validates that the property value is not empty.
     * Works with Strings, Collections, Maps, and Arrays.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getName).notEmpty();  // For strings
     * ruleFor(User::getRoles).notEmpty(); // For collections
     * }
     * </pre>
     */
    public RuleBuilder<T, R> notEmpty() {
        return notEmpty("must not be empty");
    }

    /**
     * Validates that the property value is not empty with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> notEmpty(String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    if (v instanceof String) return !((String) v).isEmpty();
                    if (v instanceof Collection) return !((Collection<?>) v).isEmpty();
                    if (v instanceof Map) return !((Map<?, ?>) v).isEmpty();
                    if (v.getClass().isArray()) return ((Object[]) v).length > 0;
                    return !v.toString().isEmpty();
                },
                message
        );
    }

    /**
     * Validates that the property value is empty.
     * Works with Strings, Collections, Maps, and Arrays.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isEmpty() {
        return isEmpty("must be empty");
    }

    /**
     * Validates that the property value is empty with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isEmpty(String message) {
        return must(
                v -> {
                    if (v == null) return true;
                    if (v instanceof String) return ((String) v).isEmpty();
                    if (v instanceof Collection) return ((Collection<?>) v).isEmpty();
                    if (v instanceof Map) return ((Map<?, ?>) v).isEmpty();
                    if (v.getClass().isArray()) return ((Object[]) v).length == 0;
                    return v.toString().isEmpty();
                },
                message
        );
    }

    /**
     * Validates that the string property value is not blank (not null, not empty, and not whitespace only).
     * Only works with String properties.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getName).notBlank();
     * }
     * </pre>
     */
    public RuleBuilder<T, R> notBlank() {
        return notBlank("must not be blank");
    }

    /**
     * Validates that the string property value is not blank with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> notBlank(String message) {
        return must(
                v -> v != null && !v.toString().trim().isEmpty(),
                message
        );
    }

    // ========== COMPARISON RULES ==========

    /**
     * Validates that the property value is equal to the specified value.
     * Uses equals() method for comparison.
     *
     * @param other The value to compare against
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getStatus).equalTo("ACTIVE");
     * }
     * </pre>
     */
    public RuleBuilder<T, R> equalTo(R other) {
        return equalTo(other, "must be equal to " + other);
    }

    /**
     * Validates that the property value is equal to the specified value with a custom message.
     *
     * @param other The value to compare against
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> equalTo(R other, String message) {
        return must(
                v -> v != null && v.equals(other),
                message
        );
    }

    /**
     * Validates that the property value is not equal to the specified value.
     *
     * @param other The value to compare against
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getUsername).notEqualTo("admin");
     * }
     * </pre>
     */
    public RuleBuilder<T, R> notEqualTo(R other) {
        return notEqualTo(other, "must not be equal to " + other);
    }

    /**
     * Validates that the property value is not equal to the specified value with a custom message.
     *
     * @param other The value to compare against
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> notEqualTo(R other, String message) {
        return must(
                v -> v == null || !v.equals(other),
                message
        );
    }

    // ========== NUMERIC COMPARISON RULES ==========

    /**
     * Validates that the numeric property value is greater than the specified minimum.
     * Works with any Number type (Integer, Double, BigDecimal, etc.).
     *
     * @param min The minimum value (exclusive)
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getAge).greaterThan(18);
     * ruleFor(Product::getPrice).greaterThan(0.0);
     * }
     * </pre>
     */
    public RuleBuilder<T, R> greaterThan(Number min) {
        return greaterThan(min, "must be greater than " + min);
    }

    /**
     * Validates that the numeric property value is greater than the specified minimum with a custom message.
     *
     * @param min The minimum value (exclusive)
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> greaterThan(Number min, String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() > min.doubleValue(),
                message
        );
    }

    /**
     * Validates that the numeric property value is greater than or equal to the specified minimum.
     *
     * @param min The minimum value (inclusive)
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getAge).greaterThanOrEqualTo(18);
     * }
     * </pre>
     */
    public RuleBuilder<T, R> greaterThanOrEqualTo(Number min) {
        return greaterThanOrEqualTo(min, "must be greater than or equal to " + min);
    }

    /**
     * Validates that the numeric property value is greater than or equal to the specified minimum with a custom message.
     *
     * @param min The minimum value (inclusive)
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> greaterThanOrEqualTo(Number min, String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() >= min.doubleValue(),
                message
        );
    }

    /**
     * Validates that the numeric property value is less than the specified maximum.
     *
     * @param max The maximum value (exclusive)
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getAge).lessThan(100);
     * }
     * </pre>
     */
    public RuleBuilder<T, R> lessThan(Number max) {
        return lessThan(max, "must be less than " + max);
    }

    /**
     * Validates that the numeric property value is less than the specified maximum with a custom message.
     *
     * @param max The maximum value (exclusive)
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> lessThan(Number max, String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() < max.doubleValue(),
                message
        );
    }

    /**
     * Validates that the numeric property value is less than or equal to the specified maximum.
     *
     * @param max The maximum value (inclusive)
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> lessThanOrEqualTo(Number max) {
        return lessThanOrEqualTo(max, "must be less than or equal to " + max);
    }

    /**
     * Validates that the numeric property value is less than or equal to the specified maximum with a custom message.
     *
     * @param max The maximum value (inclusive)
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> lessThanOrEqualTo(Number max, String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() <= max.doubleValue(),
                message
        );
    }

    /**
     * Validates that the numeric property value is between the specified minimum and maximum (inclusive).
     *
     * @param min The minimum value (inclusive)
     * @param max The maximum value (inclusive)
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getAge).inclusiveBetween(18, 65);
     * }
     * </pre>
     */
    public RuleBuilder<T, R> inclusiveBetween(Number min, Number max) {
        return inclusiveBetween(min, max, "must be between " + min + " and " + max + " (inclusive)");
    }

    /**
     * Validates that the numeric property value is between the specified minimum and maximum (inclusive) with a custom message.
     *
     * @param min The minimum value (inclusive)
     * @param max The maximum value (inclusive)
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> inclusiveBetween(Number min, Number max, String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() >= min.doubleValue() &&
                        ((Number) v).doubleValue() <= max.doubleValue(),
                message
        );
    }

    /**
     * Validates that the numeric property value is between the specified minimum and maximum (exclusive).
     *
     * @param min The minimum value (exclusive)
     * @param max The maximum value (exclusive)
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Product::getDiscount).exclusiveBetween(0, 100);
     * }
     * </pre>
     */
    public RuleBuilder<T, R> exclusiveBetween(Number min, Number max) {
        return exclusiveBetween(min, max, "must be between " + min + " and " + max + " (exclusive)");
    }

    /**
     * Validates that the numeric property value is between the specified minimum and maximum (exclusive) with a custom message.
     *
     * @param min The minimum value (exclusive)
     * @param max The maximum value (exclusive)
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> exclusiveBetween(Number min, Number max, String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() > min.doubleValue() &&
                        ((Number) v).doubleValue() < max.doubleValue(),
                message
        );
    }

    /**
     * Validates that the numeric property value is positive (greater than zero).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Product::getPrice).isPositive();
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isPositive() {
        return isPositive("must be positive");
    }

    /**
     * Validates that the numeric property value is positive with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isPositive(String message) {
        return must(
                v -> v instanceof Number && ((Number) v).doubleValue() > 0,
                message
        );
    }

    /**
     * Validates that the numeric property value is negative (less than zero).
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isNegative() {
        return isNegative("must be negative");
    }

    /**
     * Validates that the numeric property value is negative with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isNegative(String message) {
        return must(
                v -> v instanceof Number && ((Number) v).doubleValue() < 0,
                message
        );
    }

    /**
     * Validates that the numeric property value is exactly zero.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isZero() {
        return isZero("must be zero");
    }

    /**
     * Validates that the numeric property value is exactly zero with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isZero(String message) {
        return must(
                v -> v instanceof Number && ((Number) v).doubleValue() == 0,
                message
        );
    }

    /**
     * Validates that the numeric property value is not zero.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isNotZero() {
        return isNotZero("must not be zero");
    }

    /**
     * Validates that the numeric property value is not zero with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isNotZero(String message) {
        return must(
                v -> v instanceof Number && ((Number) v).doubleValue() != 0,
                message
        );
    }

    // ========== DECIMAL PRECISION RULES ==========

    /**
     * Validates that the BigDecimal property value has at most the specified precision and scale.
     * Useful for validating monetary values or other decimal numbers with specific precision requirements.
     *
     * @param precision Maximum total number of digits
     * @param scale Maximum number of decimal places
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Invoice::getAmount).precisionScale(10, 2); // Max 10 digits, 2 decimal places
     * }
     * </pre>
     */
    public RuleBuilder<T, R> precisionScale(int precision, int scale) {
        return precisionScale(precision, scale,
                "must have at most " + precision + " digits with " + scale + " decimal places");
    }

    /**
     * Validates that the BigDecimal property value has at most the specified precision and scale with a custom message.
     *
     * @param precision Maximum total number of digits
     * @param scale Maximum number of decimal places
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> precisionScale(int precision, int scale, String message) {
        return must(
                v -> {
                    if (!(v instanceof BigDecimal)) return false;
                    BigDecimal bd = (BigDecimal) v;
                    return bd.precision() <= precision && bd.scale() <= scale;
                },
                message
        );
    }

    // ========== STRING LENGTH RULES ==========

    /**
     * Validates that the string property value has exactly the specified length.
     *
     * @param exact The exact required length
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getZipCode).length(5); // Must be exactly 5 characters
     * }
     * </pre>
     */
    public RuleBuilder<T, R> length(int exact) {
        return length(exact, "must be exactly " + exact + " characters long");
    }

    /**
     * Validates that the string property value has exactly the specified length with a custom message.
     *
     * @param exact The exact required length
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> length(int exact, String message) {
        return must(
                v -> v != null && v.toString().length() == exact,
                message
        );
    }

    /**
     * Validates that the string property value length is between the specified minimum and maximum (inclusive).
     *
     * @param min Minimum length (inclusive)
     * @param max Maximum length (inclusive)
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getPassword).length(8, 20); // Between 8 and 20 characters
     * }
     * </pre>
     */
    public RuleBuilder<T, R> length(int min, int max) {
        return length(min, max, "must be between " + min + " and " + max + " characters long");
    }

    /**
     * Validates that the string property value length is between the specified minimum and maximum with a custom message.
     *
     * @param min Minimum length (inclusive)
     * @param max Maximum length (inclusive)
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> length(int min, int max, String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    int len = v.toString().length();
                    return len >= min && len <= max;
                },
                message
        );
    }

    /**
     * Validates that the string property value has at least the specified minimum length.
     *
     * @param length Minimum required length
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getName).minLength(3); // At least 3 characters
     * }
     * </pre>
     */
    public RuleBuilder<T, R> minLength(int length) {
        return minLength(length, "must be at least " + length + " characters long");
    }

    /**
     * Validates that the string property value has at least the specified minimum length with a custom message.
     *
     * @param length Minimum required length
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> minLength(int length, String message) {
        return must(
                v -> v != null && v.toString().length() >= length,
                message
        );
    }

    /**
     * Validates that the string property value has at most the specified maximum length.
     *
     * @param length Maximum allowed length
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getUsername).maxLength(50); // Maximum 50 characters
     * }
     * </pre>
     */
    public RuleBuilder<T, R> maxLength(int length) {
        return maxLength(length, "must be at most " + length + " characters long");
    }

    /**
     * Validates that the string property value has at most the specified maximum length with a custom message.
     *
     * @param length Maximum allowed length
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> maxLength(int length, String message) {
        return must(
                v -> v != null && v.toString().length() <= length,
                message
        );
    }

    // ========== COLLECTION RULES ==========

    /**
     * Validates that the collection property has at least the specified minimum number of items.
     * Works with Collections, Maps, and Arrays.
     *
     * @param min Minimum number of items
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Order::getItems).hasMinCount(1); // At least 1 item
     * }
     * </pre>
     */
    public RuleBuilder<T, R> hasMinCount(int min) {
        return hasMinCount(min, "must have at least " + min + " items");
    }

    /**
     * Validates that the collection property has at least the specified minimum number of items with a custom message.
     *
     * @param min Minimum number of items
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> hasMinCount(int min, String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    if (v instanceof Collection) return ((Collection<?>) v).size() >= min;
                    if (v instanceof Map) return ((Map<?, ?>) v).size() >= min;
                    if (v.getClass().isArray()) return ((Object[]) v).length >= min;
                    return false;
                },
                message
        );
    }

    /**
     * Validates that the collection property has at most the specified maximum number of items.
     *
     * @param max Maximum number of items
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Order::getItems).hasMaxCount(10); // Maximum 10 items
     * }
     * </pre>
     */
    public RuleBuilder<T, R> hasMaxCount(int max) {
        return hasMaxCount(max, "must have at most " + max + " items");
    }

    /**
     * Validates that the collection property has at most the specified maximum number of items with a custom message.
     *
     * @param max Maximum number of items
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> hasMaxCount(int max, String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    if (v instanceof Collection) return ((Collection<?>) v).size() <= max;
                    if (v instanceof Map) return ((Map<?, ?>) v).size() <= max;
                    if (v.getClass().isArray()) return ((Object[]) v).length <= max;
                    return false;
                },
                message
        );
    }

    /**
     * Validates that the collection property has exactly the specified number of items.
     *
     * @param count Exact number of items required
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(PhoneNumber::getDigits).hasExactCount(10); // Exactly 10 digits
     * }
     * </pre>
     */
    public RuleBuilder<T, R> hasExactCount(int count) {
        return hasExactCount(count, "must have exactly " + count + " items");
    }

    /**
     * Validates that the collection property has exactly the specified number of items with a custom message.
     *
     * @param count Exact number of items required
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> hasExactCount(int count, String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    if (v instanceof Collection) return ((Collection<?>) v).size() == count;
                    if (v instanceof Map) return ((Map<?, ?>) v).size() == count;
                    if (v.getClass().isArray()) return ((Object[]) v).length == count;
                    return false;
                },
                message
        );
    }

    /**
     * Validates that the collection property contains only unique items (no duplicates).
     * Only works with Collection properties.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getRoles).hasUniqueItems(); // No duplicate roles
     * }
     * </pre>
     */
    public RuleBuilder<T, R> hasUniqueItems() {
        return hasUniqueItems("must not contain duplicate items");
    }

    /**
     * Validates that the collection property contains only unique items with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> hasUniqueItems(String message) {
        return must(
                v -> {
                    if (!(v instanceof Collection)) return false;
                    Collection<?> collection = (Collection<?>) v;
                    return collection.size() == new HashSet<>(collection).size();
                },
                message
        );
    }

    /**
     * Validates that the collection or string property contains the specified item.
     * Works with Collections (contains method) and Strings (contains substring).
     *
     * @param item The item to check for
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getRoles).contains("ADMIN"); // Collection contains "ADMIN"
     * ruleFor(Email::getDomain).contains("@");   // String contains "@"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> contains(Object item) {
        return contains(item, "must contain " + item);
    }

    /**
     * Validates that the collection or string property contains the specified item with a custom message.
     *
     * @param item The item to check for
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> contains(Object item, String message) {
        return must(
                v -> {
                    if (v instanceof Collection) return ((Collection<?>) v).contains(item);
                    if (v instanceof String) return v.toString().contains(item.toString());
                    return false;
                },
                message
        );
    }

    /**
     * Validates that the collection or string property does not contain the specified item.
     *
     * @param item The item to check for absence
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getUsername).doesNotContain("admin"); // Username cannot contain "admin"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> doesNotContain(Object item) {
        return doesNotContain(item, "must not contain " + item);
    }

    /**
     * Validates that the collection or string property does not contain the specified item with a custom message.
     *
     * @param item The item to check for absence
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> doesNotContain(Object item, String message) {
        return must(
                v -> {
                    if (v instanceof Collection) return !((Collection<?>) v).contains(item);
                    if (v instanceof String) return !v.toString().contains(item.toString());
                    return true;
                },
                message
        );
    }

    // ========== ENUM RULES ==========

    /**
     * Validates that the string property value is a valid value of the specified enum.
     * Supports both string values that match enum names and actual enum instances.
     *
     * @param enumClass The enum class to validate against
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getStatus).isInEnum(UserStatus.class);
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isInEnum(Class<? extends Enum<?>> enumClass) {
        return isInEnum(enumClass, "must be a valid " + enumClass.getSimpleName() + " value");
    }

    /**
     * Validates that the string property value is a valid value of the specified enum with a custom message.
     *
     * @param enumClass The enum class to validate against
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isInEnum(Class<? extends Enum<?>> enumClass, String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    if (enumClass.isInstance(v)) return true;

                    try {
                        Enum.valueOf((Class) enumClass, v.toString());
                        return true;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                },
                message
        );
    }

    // ========== STRING FORMAT RULES ==========

    /**
     * Validates that the string property value is a valid email address.
     * Uses a comprehensive regex pattern for email validation.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getEmail).email();
     * }
     * </pre>
     */
    public RuleBuilder<T, R> email() {
        return email("must be a valid email address");
    }

    /**
     * Validates that the string property value is a valid email address with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> email(String message) {
        return must(
                v -> v != null && EMAIL_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value is a valid URL.
     * Uses Java's URL constructor for validation.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getWebsite).url();
     * }
     * </pre>
     */
    public RuleBuilder<T, R> url() {
        return url("must be a valid URL");
    }

    /**
     * Validates that the string property value is a valid URL with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> url(String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    String url = v.toString().trim();

                    // Basic URL structure check
                    if (url.isEmpty() || url.length() > 2048) return false;

                    // Strict URL pattern
                    return URL_PATTERN.matcher(url).matches();
                },
                message
        );
    }

    /**
     * Validates that the string property value contains only alphabetic characters (letters).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getFirstName).isAlpha(); // Only letters allowed
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isAlpha() {
        return isAlpha("must contain only letters");
    }

    /**
     * Validates that the string property value contains only alphabetic characters with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isAlpha(String message) {
        return must(
                v -> v != null && ALPHA_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value contains only numeric digits (0-9).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getZipCode).isNumeric(); // Only digits allowed
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isNumeric() {
        return isNumeric("must contain only digits");
    }

    /**
     * Validates that the string property value contains only numeric digits with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isNumeric(String message) {
        return must(
                v -> v != null && NUMERIC_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value contains only alphanumeric characters (letters and digits).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getUsername).isAlphanumeric(); // Letters and numbers only
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isAlphanumeric() {
        return isAlphanumeric("must contain only letters and digits");
    }

    /**
     * Validates that the string property value contains only alphanumeric characters with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isAlphanumeric(String message) {
        return must(
                v -> v != null && ALPHANUMERIC_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value is in uppercase (all characters are uppercase).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getCountryCode).isUpperCase(); // Must be like "US", "UK"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isUpperCase() {
        return isUpperCase("must be in uppercase");
    }

    /**
     * Validates that the string property value is in uppercase with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isUpperCase(String message) {
        return must(
                v -> v != null && v.toString().equals(v.toString().toUpperCase()),
                message
        );
    }

    /**
     * Validates that the string property value is in lowercase (all characters are lowercase).
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isLowerCase() {
        return isLowerCase("must be in lowercase");
    }

    /**
     * Validates that the string property value is in lowercase with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isLowerCase(String message) {
        return must(
                v -> v != null && v.toString().equals(v.toString().toLowerCase()),
                message
        );
    }

    /**
     * Validates that the string property value is a valid hexadecimal string.
     * Supports optional leading '#' character.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Color::getHexCode).isHexadecimal(); // Valid: "#FF5733", "abc123"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isHexadecimal() {
        return isHexadecimal("must be a valid hexadecimal value");
    }

    /**
     * Validates that the string property value is a valid hexadecimal string with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isHexadecimal(String message) {
        return must(
                v -> v != null && HEX_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value is a valid Base64 encoded string.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Image::getBase64Data).isBase64();
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isBase64() {
        return isBase64("must be a valid Base64 encoded string");
    }

    /**
     * Validates that the string property value is a valid Base64 encoded string with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isBase64(String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    String str = v.toString().trim();

                    // Data URL format
                    if (str.startsWith("data:") && str.contains(";base64,")) {
                        // Only Base64
                        str = str.substring(str.indexOf(',') + 1);
                    }

                    try {
                        Base64.getDecoder().decode(str);
                        return true;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                },
                message
        );
    }

    /**
     * Validates that the string property value is a valid UUID (Universally Unique Identifier).
     * Supports both uppercase and lowercase, with or without hyphens.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Document::getId).isUuid(); // Valid: "123e4567-e89b-12d3-a456-426614174000"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isUuid() {
        return isUuid("must be a valid UUID");
    }

    /**
     * Validates that the string property value is a valid UUID with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isUuid(String message) {
        return must(
                v -> v != null && UUID_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value contains no whitespace characters.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getUsername).containsNoWhitespace(); // No spaces allowed
     * }
     * </pre>
     */
    public RuleBuilder<T, R> containsNoWhitespace() {
        return containsNoWhitespace("must not contain whitespace");
    }

    /**
     * Validates that the string property value contains no whitespace characters with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsNoWhitespace(String message) {
        return must(
                v -> v != null && NO_WHITESPACE_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value starts with the specified prefix.
     *
     * @param prefix The required prefix
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Image::getUrl).startsWith("https://"); // Must be HTTPS URL
     * }
     * </pre>
     */
    public RuleBuilder<T, R> startsWith(String prefix) {
        return startsWith(prefix, "must start with '" + prefix + "'");
    }

    /**
     * Validates that the string property value starts with the specified prefix with a custom message.
     *
     * @param prefix The required prefix
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> startsWith(String prefix, String message) {
        return must(
                v -> v != null && v.toString().startsWith(prefix),
                message
        );
    }

    /**
     * Validates that the string property value ends with the specified suffix.
     *
     * @param suffix The required suffix
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(File::getName).endsWith(".pdf"); // Must be PDF file
     * }
     * </pre>
     */
    public RuleBuilder<T, R> endsWith(String suffix) {
        return endsWith(suffix, "must end with '" + suffix + "'");
    }

    /**
     * Validates that the string property value ends with the specified suffix with a custom message.
     *
     * @param suffix The required suffix
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> endsWith(String suffix, String message) {
        return must(
                v -> v != null && v.toString().endsWith(suffix),
                message
        );
    }

    // ========== INTERNATIONAL STANDARDS ==========

    /**
     * Validates that the string property value is a valid IBAN (International Bank Account Number).
     * Supports both formatted (with spaces) and unformatted IBANs.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(BankAccount::getIban).isIban(); // Valid: "GB82 WEST 1234 5698 7654 32"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isIban() {
        return isIban("must be a valid IBAN");
    }

    /**
     * Validates that the string property value is a valid IBAN with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isIban(String message) {
        return must(
                v -> v != null && isValidIban(v.toString()),
                message
        );
    }

    /**
     * Validates that the string property value is a valid BIC/SWIFT code.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(BankAccount::getBic).isBic(); // Valid: "DEUTDEFF"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isBic() {
        return isBic("must be a valid BIC/SWIFT code");
    }

    /**
     * Validates that the string property value is a valid BIC/SWIFT code with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isBic(String message) {
        return must(
                v -> v != null && BIC_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value is a valid ISBN (International Standard Book Number).
     * Supports both ISBN-10 and ISBN-13 formats, with or without hyphens.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Book::getIsbn).isIsbn(); // Valid: "0-306-40615-2" or "978-0-306-40615-7"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isIsbn() {
        return isIsbn("must be a valid ISBN");
    }

    /**
     * Validates that the string property value is a valid ISBN with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isIsbn(String message) {
        return must(
                v -> v != null && (isValidIsbn10(v.toString()) || isValidIsbn13(v.toString())),
                message
        );
    }

    /**
     * Validates that the string property value is a valid ISSN (International Standard Serial Number).
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isIssn() {
        return isIssn("must be a valid ISSN");
    }

    /**
     * Validates that the string property value is a valid ISSN with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isIssn(String message) {
        return must(
                v -> v != null && ISSN_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value is a valid credit card number using the Luhn algorithm.
     * Removes spaces before validation.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Payment::getCardNumber).creditCard(); // Valid: "4532015112830366"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> creditCard() {
        return creditCard("must be a valid credit card number");
    }

    /**
     * Validates that the string property value is a valid credit card number with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> creditCard(String message) {
        return must(
                v -> v != null && isValidLuhn(v.toString()),
                message
        );
    }

    // ========== PHONE & NETWORK ==========

    /**
     * Validates that the string property value is a valid phone number.
     * Supports international formats with country codes and various separators.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Contact::getPhone).isPhoneNumber(); // Valid: "+1-555-123-4567", "+90 555 123 4567"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isPhoneNumber() {
        return isPhoneNumber("must be a valid phone number");
    }

    /**
     * Validates that the string property value is a valid phone number with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isPhoneNumber(String message) {
        return must(
                v -> v != null && isValidPhoneNumber(v.toString()),
                message
        );
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        String trimmed = phone.trim();

        // Number count test (between 6-15)
        String digitsOnly = trimmed.replaceAll("[^\\d]", "");
        int digitCount = digitsOnly.length();

        // European numbers can be longer with country code
        if (digitCount < 6 || digitCount > 20) {
            return false;
        }

        return PHONE_NUMBER_PATTERN.matcher(trimmed).matches();
    }

    /**
     * Validates that the string property value is a valid IP address (IPv4 or IPv6).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Server::getIpAddress).isIpAddress(); // Valid: "192.168.1.1" or "2001:0db8::1"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isIpAddress() {
        return isIpAddress("must be a valid IP address");
    }

    /**
     * Validates that the string property value is a valid IP address with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isIpAddress(String message) {
        return must(
                v -> v != null && (IPV4_PATTERN.matcher(v.toString()).matches() ||
                        IPV6_PATTERN.matcher(v.toString()).matches()),
                message
        );
    }

    /**
     * Validates that the string property value is a valid IPv4 address.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isIpv4() {
        return isIpv4("must be a valid IPv4 address");
    }

    /**
     * Validates that the string property value is a valid IPv4 address with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isIpv4(String message) {
        return must(
                v -> v != null && IPV4_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value is a valid IPv6 address.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isIpv6() {
        return isIpv6("must be a valid IPv6 address");
    }

    /**
     * Validates that the string property value is a valid IPv6 address with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isIpv6(String message) {
        return must(
                v -> v != null && IPV6_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value is a valid MAC address (Media Access Control address).
     * Supports various formats (colons, dashes, dots, or no separators).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Device::getMacAddress).isMacAddress(); // Valid: "00:1B:44:11:3A:B7"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isMacAddress() {
        return isMacAddress("must be a valid MAC address");
    }

    /**
     * Validates that the string property value is a valid MAC address with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isMacAddress(String message) {
        return must(
                v -> v != null && MAC_ADDRESS_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    // ========== DATE & TIME RULES ==========

    /**
     * Validates that the date property value is in the past (before today).
     * Works with LocalDate, LocalDateTime, and java.util.Date.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getBirthDate).isInPast(); // Birth date must be in the past
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isInPast() {
        return isInPast("must be in the past");
    }

    /**
     * Validates that the date property value is in the past with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isInPast(String message) {
        return must(
                v -> {
                    if (v instanceof LocalDate) return ((LocalDate) v).isBefore(LocalDate.now());
                    if (v instanceof LocalDateTime) return ((LocalDateTime) v).isBefore(LocalDateTime.now());
                    if (v instanceof Date) return ((Date) v).before(new Date());
                    return false;
                },
                message
        );
    }

    /**
     * Validates that the date property value is in the future (after today).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Appointment::getDate).isInFuture(); // Appointment must be in the future
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isInFuture() {
        return isInFuture("must be in the future");
    }

    /**
     * Validates that the date property value is in the future with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isInFuture(String message) {
        return must(
                v -> {
                    if (v instanceof LocalDate) return ((LocalDate) v).isAfter(LocalDate.now());
                    if (v instanceof LocalDateTime) return ((LocalDateTime) v).isAfter(LocalDateTime.now());
                    if (v instanceof Date) return ((Date) v).after(new Date());
                    return false;
                },
                message
        );
    }

    /**
     * Validates that the date property value is today.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Task::getDueDate).isToday(); // Task is due today
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isToday() {
        return isToday("must be today");
    }

    /**
     * Validates that the date property value is today with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isToday(String message) {
        return must(
                v -> {
                    if (v instanceof LocalDate) return ((LocalDate) v).equals(LocalDate.now());
                    if (v instanceof LocalDateTime) return ((LocalDateTime) v).toLocalDate().equals(LocalDate.now());
                    return false;
                },
                message
        );
    }

    /**
     * Validates that the date property value represents a person who is at least the specified minimum age.
     * Calculates age based on the current date.
     *
     * @param years Minimum age in years
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getBirthDate).minAge(18); // Must be at least 18 years old
     * }
     * </pre>
     */
    public RuleBuilder<T, R> minAge(int years) {
        return minAge(years, "must be at least " + years + " years old");
    }

    /**
     * Validates that the date property value represents a person who is at least the specified minimum age with a custom message.
     *
     * @param years Minimum age in years
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> minAge(int years, String message) {
        return must(
                v -> {
                    if (v instanceof LocalDate) {
                        return ChronoUnit.YEARS.between((LocalDate) v, LocalDate.now()) >= years;
                    }
                    return false;
                },
                message
        );
    }

    /**
     * Validates that the date property value represents a person who is at most the specified maximum age.
     *
     * @param years Maximum age in years
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> maxAge(int years) {
        return maxAge(years, "must be at most " + years + " years old");
    }

    /**
     * Validates that the date property value represents a person who is at most the specified maximum age with a custom message.
     *
     * @param years Maximum age in years
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> maxAge(int years, String message) {
        return must(
                v -> {
                    if (v instanceof LocalDate) {
                        return ChronoUnit.YEARS.between((LocalDate) v, LocalDate.now()) <= years;
                    }
                    return false;
                },
                message
        );
    }

    // ========== COORDINATE RULES ==========

    /**
     * Validates that the numeric property value is a valid latitude (-90 to 90 degrees).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Location::getLatitude).isLatitude(); // Valid: 45.5, -33.86
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isLatitude() {
        return isLatitude("must be a valid latitude (-90 to 90)");
    }

    /**
     * Validates that the numeric property value is a valid latitude with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isLatitude(String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() >= -90 &&
                        ((Number) v).doubleValue() <= 90,
                message
        );
    }

    /**
     * Validates that the numeric property value is a valid longitude (-180 to 180 degrees).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Location::getLongitude).isLongitude(); // Valid: 120.0, -74.0
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isLongitude() {
        return isLongitude("must be a valid longitude (-180 to 180)");
    }

    /**
     * Validates that the numeric property value is a valid longitude with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isLongitude(String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() >= -180 &&
                        ((Number) v).doubleValue() <= 180,
                message
        );
    }

    // ========== PASSWORD RULES ==========

    /**
     * Validates that the string property value contains at least one uppercase letter.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getPassword).containsUppercase();
     * }
     * </pre>
     */
    public RuleBuilder<T, R> containsUppercase() {
        return containsUppercase("must contain at least one uppercase letter");
    }

    /**
     * Validates that the string property value contains at least one uppercase letter with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsUppercase(String message) {
        return must(
                v -> v != null && v.toString().chars().anyMatch(Character::isUpperCase),
                message
        );
    }

    /**
     * Validates that the string property value contains at least one lowercase letter.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsLowercase() {
        return containsLowercase("must contain at least one lowercase letter");
    }

    /**
     * Validates that the string property value contains at least one lowercase letter with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsLowercase(String message) {
        return must(
                v -> v != null && v.toString().chars().anyMatch(Character::isLowerCase),
                message
        );
    }

    /**
     * Validates that the string property value contains at least one digit (0-9).
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsDigit() {
        return containsDigit("must contain at least one digit");
    }

    /**
     * Validates that the string property value contains at least one digit with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsDigit(String message) {
        return must(
                v -> v != null && v.toString().chars().anyMatch(Character::isDigit),
                message
        );
    }

    /**
     * Validates that the string property value contains at least one special character
     * (any character that is not a letter or digit).
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsSpecialChar() {
        return containsSpecialChar("must contain at least one special character");
    }

    /**
     * Validates that the string property value contains at least one special character with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsSpecialChar(String message) {
        return must(
                v -> v != null && v.toString().chars()
                        .anyMatch(ch -> !Character.isLetterOrDigit(ch)),
                message
        );
    }

    /**
     * Validates that the string property value meets strong password criteria:
     * - Minimum and maximum length
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     * Uses default error messages from PasswordMessages.
     *
     * @param minLength Minimum password length
     * @param maxLength Maximum password length
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getPassword).strongPassword(8, 20);
     * }
     * </pre>
     */
    public RuleBuilder<T, R> strongPassword(Number minLength, Number maxLength) {
        return strongPassword(
                minLength,
                maxLength,
                PasswordMessages.defaults()
        );
    }

    /**
     * Validates that the string property value meets strong password criteria with custom messages.
     *
     * @param minLength Minimum password length
     * @param maxLength Maximum password length
     * @param messages Custom password error messages
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> strongPassword(Number minLength, Number maxLength, PasswordMessages messages) {
        return this
                .minLength(minLength.intValue(), messages.minLength(minLength.intValue()))
                .maxLength(maxLength.intValue(), messages.maxLength(maxLength.intValue()))
                .containsUppercase(messages.uppercase())
                .containsLowercase(messages.lowercase())
                .containsDigit(messages.digit())
                .containsSpecialChar(messages.specialChar());
    }

    // ========== REGEX RULES ==========

    /**
     * Validates that the string property value matches the specified regular expression.
     *
     * @param regex The regular expression pattern
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::getUsername).matches("^[a-zA-Z0-9_]{3,20}$");
     * }
     * </pre>
     */
    public RuleBuilder<T, R> matches(String regex) {
        return matches(regex, "must match pattern: " + regex);
    }

    /**
     * Validates that the string property value matches the specified regular expression with a custom message.
     *
     * @param regex The regular expression pattern
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> matches(String regex, String message) {
        return matches(Pattern.compile(regex), message);
    }

    /**
     * Validates that the string property value matches the specified compiled Pattern.
     *
     * @param pattern The compiled Pattern object
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> matches(Pattern pattern) {
        return matches(pattern, "must match pattern: " + pattern.pattern());
    }

    /**
     * Validates that the string property value matches the specified compiled Pattern with a custom message.
     *
     * @param pattern The compiled Pattern object
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> matches(Pattern pattern, String message) {
        return must(
                v -> v != null && pattern.matcher(v.toString()).matches(),
                message
        );
    }

    // ========== HELPER METHODS ==========

    /**
     * Validates a credit card number using the Luhn algorithm.
     *
     * @param number The credit card number (spaces are removed)
     * @return true if the number passes the Luhn check
     */
    private boolean isValidLuhn(String number) {
        String cleaned = number.replaceAll("\\s+", "");
        if (!cleaned.matches("\\d+")) return false;

        int sum = 0;
        boolean alternate = false;
        for (int i = cleaned.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cleaned.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    /**
     * Validates an IBAN using the official IBAN validation algorithm.
     *
     * @param iban The IBAN to validate (spaces are removed)
     * @return true if the IBAN is valid
     */
    private boolean isValidIban(String iban) {
        String cleaned = iban.replaceAll("\\s+", "").toUpperCase();
        if (cleaned.length() < 15 || cleaned.length() > 34) return false;
        if (!cleaned.matches("[A-Z]{2}\\d{2}[A-Z0-9]+")) return false;

        String rearranged = cleaned.substring(4) + cleaned.substring(0, 4);
        StringBuilder numeric = new StringBuilder();
        for (char c : rearranged.toCharArray()) {
            if (Character.isLetter(c)) {
                numeric.append(c - 'A' + 10);
            } else {
                numeric.append(c);
            }
        }

        BigDecimal ibanNumber = new BigDecimal(numeric.toString());
        return ibanNumber.remainder(new BigDecimal(97)).intValue() == 1;
    }

    /**
     * Validates an ISBN-10 using the official ISBN-10 validation algorithm.
     *
     * @param isbn The ISBN-10 to validate (hyphens are removed)
     * @return true if the ISBN-10 is valid
     */
    private boolean isValidIsbn10(String isbn) {
        String cleaned = isbn.replaceAll("-", "");
        if (cleaned.length() != 10) return false;

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(cleaned.charAt(i))) return false;
            sum += (cleaned.charAt(i) - '0') * (10 - i);
        }

        char last = cleaned.charAt(9);
        if (last == 'X') {
            sum += 10;
        } else if (Character.isDigit(last)) {
            sum += (last - '0');
        } else {
            return false;
        }

        return sum % 11 == 0;
    }

    /**
     * Validates an ISBN-13 using the official ISBN-13 validation algorithm.
     *
     * @param isbn The ISBN-13 to validate (hyphens are removed)
     * @return true if the ISBN-13 is valid
     */
    private boolean isValidIsbn13(String isbn) {
        String cleaned = isbn.replaceAll("-", "");
        if (cleaned.length() != 13 || !cleaned.matches("\\d+")) return false;

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = cleaned.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        return checkDigit == (cleaned.charAt(12) - '0');
    }

    // ========== PORT VALIDATION ==========

    /**
     * Validates that the numeric property value is a valid port number (0-65535).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Server::getPort).isPort(); // Valid: 80, 443, 8080
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isPort() {
        return isPort("must be a valid port number (0-65535)");
    }

    /**
     * Validates that the numeric property value is a valid port number with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isPort(String message) {
        return must(
                v -> {
                    if (!(v instanceof Number)) return false;
                    int port = ((Number) v).intValue();
                    return port >= 0 && port <= 65535;
                },
                message
        );
    }

    // ========== BYTE SIZE VALIDATION ==========

    /**
     * Validates that the numeric property value is at most the specified size in bytes.
     *
     * @param maxBytes Maximum size in bytes
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(File::getSize).maxSizeInBytes(10 * 1024 * 1024); // Max 10 MB
     * }
     * </pre>
     */
    public RuleBuilder<T, R> maxSizeInBytes(long maxBytes) {
        return maxSizeInBytes(maxBytes, "must be at most " + formatBytes(maxBytes));
    }

    /**
     * Validates that the numeric property value is at most the specified size in bytes with a custom message.
     *
     * @param maxBytes Maximum size in bytes
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> maxSizeInBytes(long maxBytes, String message) {
        return must(
                v -> v instanceof Number && ((Number) v).longValue() <= maxBytes,
                message
        );
    }

    /**
     * Validates that the numeric property value is at most the specified size in kilobytes.
     *
     * @param maxKB Maximum size in kilobytes
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> maxSizeInKB(long maxKB) {
        return maxSizeInBytes(maxKB * 1024, "must be at most " + maxKB + " KB");
    }

    /**
     * Validates that the numeric property value is at most the specified size in megabytes.
     *
     * @param maxMB Maximum size in megabytes
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> maxSizeInMB(long maxMB) {
        return maxSizeInBytes(maxMB * 1024 * 1024, "must be at most " + maxMB + " MB");
    }

    /**
     * Validates that the numeric property value is at most the specified size in gigabytes.
     *
     * @param maxGB Maximum size in gigabytes
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> maxSizeInGB(long maxGB) {
        return maxSizeInBytes(maxGB * 1024 * 1024 * 1024, "must be at most " + maxGB + " GB");
    }

    // ========== PERCENTAGE VALIDATION ==========

    /**
     * Validates that the numeric property value is a valid percentage (0-100).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Discount::getPercentage).isPercentage(); // Valid: 0, 50, 100
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isPercentage() {
        return isPercentage("must be a valid percentage (0-100)");
    }

    /**
     * Validates that the numeric property value is a valid percentage with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isPercentage(String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).doubleValue() >= 0 &&
                        ((Number) v).doubleValue() <= 100,
                message
        );
    }

    // ========== DIVISIBLE BY VALIDATION ==========

    /**
     * Validates that the numeric property value is divisible by the specified divisor.
     *
     * @param divisor The divisor
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Package::getQuantity).isDivisibleBy(12); // Must be in dozens
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isDivisibleBy(Number divisor) {
        return isDivisibleBy(divisor, "must be divisible by " + divisor);
    }

    /**
     * Validates that the numeric property value is divisible by the specified divisor with a custom message.
     *
     * @param divisor The divisor
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isDivisibleBy(Number divisor, String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).longValue() % divisor.longValue() == 0,
                message
        );
    }

    /**
     * Validates that the numeric property value is an even number.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isEven() {
        return isEven("must be an even number");
    }

    /**
     * Validates that the numeric property value is an even number with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isEven(String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).longValue() % 2 == 0,
                message
        );
    }

    /**
     * Validates that the numeric property value is an odd number.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isOdd() {
        return isOdd("must be an odd number");
    }

    /**
     * Validates that the numeric property value is an odd number with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isOdd(String message) {
        return must(
                v -> v instanceof Number &&
                        ((Number) v).longValue() % 2 != 0,
                message
        );
    }

    // ========== BOOLEAN VALIDATION ==========

    /**
     * Validates that the boolean property value is true.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(User::isAgreedToTerms).isTrue(); // Must agree to terms
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isTrue() {
        return isTrue("must be true");
    }

    /**
     * Validates that the boolean property value is true with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isTrue(String message) {
        return must(
                v -> v instanceof Boolean && (Boolean) v,
                message
        );
    }

    /**
     * Validates that the boolean property value is false.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isFalse() {
        return isFalse("must be false");
    }

    /**
     * Validates that the boolean property value is false with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isFalse(String message) {
        return must(
                v -> v instanceof Boolean && !(Boolean) v,
                message
        );
    }

    // ========== STRING CONTENT VALIDATION ==========

    /**
     * Validates that the string property value contains only characters from the specified allowed set.
     *
     * @param allowedChars String containing all allowed characters
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Code::getValue).containsOnly("ABCDEF1234567890"); // Hexadecimal characters only
     * }
     * </pre>
     */
    public RuleBuilder<T, R> containsOnly(String allowedChars) {
        return containsOnly(allowedChars, "must contain only these characters: " + allowedChars);
    }

    /**
     * Validates that the string property value contains only characters from the specified allowed set with a custom message.
     *
     * @param allowedChars String containing all allowed characters
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsOnly(String allowedChars, String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    String str = v.toString();
                    for (char c : str.toCharArray()) {
                        if (allowedChars.indexOf(c) == -1) {
                            return false;
                        }
                    }
                    return true;
                },
                message
        );
    }

    /**
     * Validates that the string property value does not contain any of the specified forbidden characters.
     *
     * @param forbiddenChars String containing forbidden characters
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> doesNotContainAny(String forbiddenChars) {
        return doesNotContainAny(forbiddenChars, "must not contain any of these characters: " + forbiddenChars);
    }

    /**
     * Validates that the string property value does not contain any of the specified forbidden characters with a custom message.
     *
     * @param forbiddenChars String containing forbidden characters
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> doesNotContainAny(String forbiddenChars, String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    String str = v.toString();
                    for (char c : str.toCharArray()) {
                        if (forbiddenChars.indexOf(c) != -1) {
                            return false;
                        }
                    }
                    return true;
                },
                message
        );
    }

    /**
     * Validates that the string property value contains at least the specified minimum number of words.
     * Words are separated by whitespace.
     *
     * @param minWords Minimum number of words required
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Description::getText).hasMinWords(10); // At least 10 words
     * }
     * </pre>
     */
    public RuleBuilder<T, R> hasMinWords(int minWords) {
        return hasMinWords(minWords, "must contain at least " + minWords + " words");
    }

    /**
     * Validates that the string property value contains at least the specified minimum number of words with a custom message.
     *
     * @param minWords Minimum number of words required
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> hasMinWords(int minWords, String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    String[] words = v.toString().trim().split("\\s+");
                    return words.length >= minWords && !words[0].isEmpty();
                },
                message
        );
    }

    /**
     * Validates that the string property value contains at most the specified maximum number of words.
     *
     * @param maxWords Maximum number of words allowed
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> hasMaxWords(int maxWords) {
        return hasMaxWords(maxWords, "must contain at most " + maxWords + " words");
    }

    /**
     * Validates that the string property value contains at most the specified maximum number of words with a custom message.
     *
     * @param maxWords Maximum number of words allowed
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> hasMaxWords(int maxWords, String message) {
        return must(
                v -> {
                    if (v == null) return false;
                    String[] words = v.toString().trim().split("\\s+");
                    return words.length <= maxWords;
                },
                message
        );
    }

    // ========== CASE VALIDATION ==========

    /**
     * Validates that the string property value is in camelCase format (first letter lowercase, no separators).
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Variable::getName).isCamelCase(); // Valid: "firstName", "userName123"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isCamelCase() {
        return isCamelCase("must be in camelCase format");
    }

    /**
     * Validates that the string property value is in camelCase format with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isCamelCase(String message) {
        return must(
                v -> v != null && CAMEL_CASE_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value is in PascalCase format (first letter uppercase, no separators).
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isPascalCase() {
        return isPascalCase("must be in PascalCase format");
    }

    /**
     * Validates that the string property value is in PascalCase format with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isPascalCase(String message) {
        return must(
                v -> v != null && PASCAL_CASE_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value is in snake_case format (lowercase with underscores).
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isSnakeCase() {
        return isSnakeCase("must be in snake_case format");
    }

    /**
     * Validates that the string property value is in snake_case format with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isSnakeCase(String message) {
        return must(
                v -> v != null && SNAKE_CASE_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value is in kebab-case format (lowercase with hyphens).
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isKebabCase() {
        return isKebabCase("must be in kebab-case format");
    }

    /**
     * Validates that the string property value is in kebab-case format with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isKebabCase(String message) {
        return must(
                v -> v != null && KEBAB_CASE_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    // ========== COLOR VALIDATION ==========

    /**
     * Validates that the string property value is a valid hex color code.
     * Supports both 3-digit (#RGB) and 6-digit (#RRGGBB) formats, with or without leading '#'.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Theme::getColor).isHexColor(); // Valid: "#FF5733", "#abc", "123456"
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isHexColor() {
        return isHexColor("must be a valid hex color code");
    }

    /**
     * Validates that the string property value is a valid hex color code with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isHexColor(String message) {
        return must(
                v -> v != null && HEX_COLOR_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    // ========== REGEX PATTERN VALIDATION ==========

    /**
     * Validates that the string property value contains only ASCII characters.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isASCII() {
        return isASCII("must contain only ASCII characters");
    }

    /**
     * Validates that the string property value contains only ASCII characters with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isASCII(String message) {
        return must(
                v -> v != null && ASCII_PATTERN.matcher(v.toString()).matches(),
                message
        );
    }

    /**
     * Validates that the string property value contains the specified regex pattern.
     * Unlike matches(), this checks if the pattern exists anywhere in the string.
     *
     * @param regex The regular expression pattern to search for
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsPattern(String regex) {
        return containsPattern(regex, "must contain pattern: " + regex);
    }

    /**
     * Validates that the string property value contains the specified regex pattern with a custom message.
     *
     * @param regex The regular expression pattern to search for
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsPattern(String regex, String message) {
        return must(
                v -> v != null && Pattern.compile(regex).matcher(v.toString()).find(),
                message
        );
    }

    /**
     * Validates that the string property value does not match the specified regex pattern.
     *
     * @param regex The regular expression pattern to avoid
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> doesNotMatchPattern(String regex) {
        return doesNotMatchPattern(regex, "must not match pattern: " + regex);
    }

    /**
     * Validates that the string property value does not match the specified regex pattern with a custom message.
     *
     * @param regex The regular expression pattern to avoid
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> doesNotMatchPattern(String regex, String message) {
        return must(
                v -> v == null || !Pattern.compile(regex).matcher(v.toString()).matches(),
                message
        );
    }

    // ========== DATE RANGE VALIDATION ==========

    /**
     * Validates that the date property value is after the specified date.
     *
     * @param date The date to compare against
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Event::getStartDate).isAfter(LocalDate.now());
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isAfter(LocalDate date) {
        return isAfter(date, "must be after " + date);
    }

    /**
     * Validates that the date property value is after the specified date with a custom message.
     *
     * @param date The date to compare against
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isAfter(LocalDate date, String message) {
        return must(
                v -> v instanceof LocalDate && ((LocalDate) v).isAfter(date),
                message
        );
    }

    /**
     * Validates that the date property value is before the specified date.
     *
     * @param date The date to compare against
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isBefore(LocalDate date) {
        return isBefore(date, "must be before " + date);
    }

    /**
     * Validates that the date property value is before the specified date with a custom message.
     *
     * @param date The date to compare against
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isBefore(LocalDate date, String message) {
        return must(
                v -> v instanceof LocalDate && ((LocalDate) v).isBefore(date),
                message
        );
    }

    /**
     * Validates that the date property value is between the specified start and end dates (inclusive).
     *
     * @param start Start date (inclusive)
     * @param end End date (inclusive)
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * LocalDate seasonStart = LocalDate.of(2024, 6, 1);
     * LocalDate seasonEnd = LocalDate.of(2024, 8, 31);
     * ruleFor(Booking::getDate).isBetweenDates(seasonStart, seasonEnd);
     * }
     * </pre>
     */
    public RuleBuilder<T, R> isBetweenDates(LocalDate start, LocalDate end) {
        return isBetweenDates(start, end, "must be between " + start + " and " + end);
    }

    /**
     * Validates that the date property value is between the specified start and end dates with a custom message.
     *
     * @param start Start date (inclusive)
     * @param end End date (inclusive)
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isBetweenDates(LocalDate start, LocalDate end, String message) {
        return must(
                v -> {
                    if (!(v instanceof LocalDate)) return false;
                    LocalDate date = (LocalDate) v;
                    return !date.isBefore(start) && !date.isAfter(end);
                },
                message
        );
    }

    /**
     * Validates that the date property value is a weekday (Monday-Friday).
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isWeekday() {
        return isWeekday("must be a weekday (Monday-Friday)");
    }

    /**
     * Validates that the date property value is a weekday with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isWeekday(String message) {
        return must(
                v -> {
                    if (!(v instanceof LocalDate)) return false;
                    int dayOfWeek = ((LocalDate) v).getDayOfWeek().getValue();
                    return dayOfWeek >= 1 && dayOfWeek <= 5;
                },
                message
        );
    }

    /**
     * Validates that the date property value is a weekend (Saturday or Sunday).
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isWeekend() {
        return isWeekend("must be a weekend (Saturday or Sunday)");
    }

    /**
     * Validates that the date property value is a weekend with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> isWeekend(String message) {
        return must(
                v -> {
                    if (!(v instanceof LocalDate)) return false;
                    int dayOfWeek = ((LocalDate) v).getDayOfWeek().getValue();
                    return dayOfWeek == 6 || dayOfWeek == 7;
                },
                message
        );
    }

    // ========== COLLECTION ADVANCED VALIDATION ==========

    /**
     * Validates that all items in the collection property match the specified predicate.
     *
     * @param itemPredicate Predicate to test each item
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(Students::getGrades).allMatch(grade -> grade >= 50);
     * }
     * </pre>
     */
    public RuleBuilder<T, R> allMatch(Predicate<?> itemPredicate) {
        return allMatch(itemPredicate, "all items must match the condition");
    }

    /**
     * Validates that all items in the collection property match the specified predicate with a custom message.
     *
     * @param itemPredicate Predicate to test each item
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> allMatch(Predicate<?> itemPredicate, String message) {
        return must(
                v -> {
                    if (!(v instanceof Collection)) return false;
                    Collection<?> collection = (Collection<?>) v;
                    return collection.stream().allMatch((Predicate<? super Object>) itemPredicate);
                },
                message
        );
    }

    /**
     * Validates that at least one item in the collection property matches the specified predicate.
     *
     * @param itemPredicate Predicate to test each item
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> anyMatch(Predicate<?> itemPredicate) {
        return anyMatch(itemPredicate, "at least one item must match the condition");
    }

    /**
     * Validates that at least one item in the collection property matches the specified predicate with a custom message.
     *
     * @param itemPredicate Predicate to test each item
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> anyMatch(Predicate<?> itemPredicate, String message) {
        return must(
                v -> {
                    if (!(v instanceof Collection)) return false;
                    Collection<?> collection = (Collection<?>) v;
                    return collection.stream().anyMatch((Predicate<? super Object>) itemPredicate);
                },
                message
        );
    }

    /**
     * Validates that no items in the collection property match the specified predicate.
     *
     * @param itemPredicate Predicate to test each item
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> noneMatch(Predicate<?> itemPredicate) {
        return noneMatch(itemPredicate, "no items must match the condition");
    }

    /**
     * Validates that no items in the collection property match the specified predicate with a custom message.
     *
     * @param itemPredicate Predicate to test each item
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> noneMatch(Predicate<?> itemPredicate, String message) {
        return must(
                v -> {
                    if (!(v instanceof Collection)) return false;
                    Collection<?> collection = (Collection<?>) v;
                    return collection.stream().noneMatch((Predicate<? super Object>) itemPredicate);
                },
                message
        );
    }

    // ========== SQL INJECTION PREVENTION ==========

    /**
     * Validates that the string property value does not contain SQL injection patterns.
     * Checks for common SQL keywords and patterns used in SQL injection attacks.
     *
     * @return This RuleBuilder for chaining
     *
     * @example
     * <pre>
     * {@code
     * ruleFor(UserInput::getSearchTerm).containsNoSqlInjection();
     * }
     * </pre>
     */
    public RuleBuilder<T, R> containsNoSqlInjection() {
        return containsNoSqlInjection("must not contain SQL injection patterns");
    }

    /**
     * Validates that the string property value does not contain SQL injection patterns with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsNoSqlInjection(String message) {
        return must(
                v -> {
                    if (v == null) return true;
                    String str = v.toString().toLowerCase();

                    // Dangerous SQL keywords
                    String[] sqlKeywords = {
                            "select ", "insert ", "update ", "delete ", "drop ",
                            "create ", "alter ", "exec ", "execute ", "union ",
                            "' or ", "\" or ", "1=1", "1 = 1", "or 1=1",
                            "--", "/*", "*/", "xp_", "sp_", "0x"
                    };

                    for (String keyword : sqlKeywords) {
                        if (str.contains(keyword)) {
                            return false;
                        }
                    }
                    return true;
                },
                message
        );
    }

    // ========== XSS PREVENTION ==========

    /**
     * Validates that the string property value does not contain XSS (Cross-Site Scripting) attack patterns.
     * Checks for common XSS vectors like script tags, javascript: URLs, and event handlers.
     *
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsNoXss() {
        return containsNoXss("must not contain XSS attack patterns");
    }

    /**
     * Validates that the string property value does not contain XSS attack patterns with a custom message.
     *
     * @param message Custom error message
     * @return This RuleBuilder for chaining
     */
    public RuleBuilder<T, R> containsNoXss(String message) {
        return must(
                v -> {
                    if (v == null) return true;
                    String str = v.toString().toLowerCase();

                    // Dangerous XSS patterns
                    String[] xssPatterns = {
                            "<script", "javascript:", "onerror=", "onload=",
                            "onclick=", "onmouseover=", "<iframe", "eval(",
                            "expression(", "vbscript:", "data:text/html"
                    };

                    for (String pattern : xssPatterns) {
                        if (str.contains(pattern)) {
                            return false;
                        }
                    }
                    return true;
                },
                message
        );
    }
    /**
     * Validates that a property does not contain command injection patterns.
     * This method checks for dangerous characters and sequences commonly used in command injection attacks.
     *
     * <p><b>Validates against:</b> ; & | ` $ ( ) < > \\n \\r && ||</p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * {@code
     * ruleFor(User::getInputField)
     *     .containsNoCommandInjection()
     *     .notNull();
     * }
     * </pre>
     *
     * @return The current RuleBuilder instance for method chaining
     * @see #containsNoCommandInjection(String)
     */
    public RuleBuilder<T, R> containsNoCommandInjection() {
        return containsNoCommandInjection("must not contain command injection patterns");
    }

    /**
     * Validates that a property does not contain command injection patterns with a custom error message.
     * This method checks for dangerous characters and sequences commonly used in command injection attacks.
     *
     * <p><b>Validates against:</b> ; & | ` $ ( ) < > \\n \\r && ||</p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * {@code
     * ruleFor(User::getInputField)
     *     .containsNoCommandInjection("Input contains dangerous characters that could lead to command injection")
     *     .notNull();
     * }
     * </pre>
     *
     * @param message The custom error message to display if validation fails
     * @return The current RuleBuilder instance for method chaining
     */
    public RuleBuilder<T, R> containsNoCommandInjection(String message) {
        return must(
                v -> {
                    if (v == null) return true;
                    String str = v.toString();

                    // Dangerous command injection characters
                    String[] dangerousChars = {
                            ";", "&", "|", "`", "$", "(", ")",
                            "<", ">", "\n", "\r", "&&", "||"
                    };

                    for (String chr : dangerousChars) {
                        if (str.contains(chr)) {
                            return false;
                        }
                    }
                    return true;
                },
                message
        );
    }

    // ========== LDAP INJECTION PREVENTION ==========

    /**
     * Validates that a property does not contain LDAP injection patterns.
     * This method checks for characters that could be used in LDAP injection attacks.
     *
     * <p><b>Validates against:</b> * ( ) \\ / NUL</p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * {@code
     * ruleFor(User::getLdapQuery)
     *     .containsNoLdapInjection()
     *     .notNull();
     * }
     * </pre>
     *
     * @return The current RuleBuilder instance for method chaining
     * @see #containsNoLdapInjection(String)
     */
    public RuleBuilder<T, R> containsNoLdapInjection() {
        return containsNoLdapInjection("must not contain LDAP injection patterns");
    }

    /**
     * Validates that a property does not contain LDAP injection patterns with a custom error message.
     * This method checks for characters that could be used in LDAP injection attacks.
     *
     * <p><b>Validates against:</b> * ( ) \\ / NUL</p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * {@code
     * ruleFor(User::getLdapQuery)
     *     .containsNoLdapInjection("Input contains characters that could lead to LDAP injection")
     *     .notNull();
     * }
     * </pre>
     *
     * @param message The custom error message to display if validation fails
     * @return The current RuleBuilder instance for method chaining
     */
    public RuleBuilder<T, R> containsNoLdapInjection(String message) {
        return must(
                v -> {
                    if (v == null) return true;
                    String str = v.toString();

                    // LDAP injection characters
                    String[] ldapChars = {
                            "*", "(", ")", "\\", "/", "NUL"
                    };

                    for (String chr : ldapChars) {
                        if (str.contains(chr)) {
                            return false;
                        }
                    }
                    return true;
                },
                message
        );
    }

    // ========== HELPER METHODS ==========

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " bytes";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)) + " MB";
        return (bytes / (1024 * 1024 * 1024)) + " GB";
    }

    // ========== REGEX PATTERNS ==========

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9][-A-Za-z0-9]*\\.)+[A-Za-z]{2,}$");

    private static final Pattern ALPHA_PATTERN =
            Pattern.compile("^[a-zA-Z]+$");

    private static final Pattern NUMERIC_PATTERN =
            Pattern.compile("^[0-9]+$");

    private static final Pattern ALPHANUMERIC_PATTERN =
            Pattern.compile("^[a-zA-Z0-9]+$");

    private static final Pattern HEX_PATTERN =
            Pattern.compile("^#?[a-fA-F0-9]+$");

    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$|^[0-9a-fA-F]{32}$");

    private static final Pattern NO_WHITESPACE_PATTERN =
            Pattern.compile("^\\S*$");

    private static final Pattern BIC_PATTERN =
            Pattern.compile("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$");

    private static final Pattern ISSN_PATTERN =
            Pattern.compile("^\\d{4}-\\d{3}[\\dX]$");

    private static final Pattern IPV4_PATTERN =
            Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");

    private static final Pattern IPV6_PATTERN =
            Pattern.compile("^(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$");

    private static final Pattern MAC_ADDRESS_PATTERN =
            Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$|^([0-9A-Fa-f]{4}\\.){2}[0-9A-Fa-f]{4}$|^[0-9A-Fa-f]{12}$");

    private static final Pattern CAMEL_CASE_PATTERN =
            Pattern.compile("^[a-z][a-zA-Z0-9]*$");

    private static final Pattern PASCAL_CASE_PATTERN =
            Pattern.compile("^[A-Z][a-zA-Z0-9]*$");

    private static final Pattern SNAKE_CASE_PATTERN =
            Pattern.compile("^[a-z][a-z0-9]*(_[a-z0-9]+)*$|^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");

    private static final Pattern KEBAB_CASE_PATTERN =
            Pattern.compile("^[a-z][a-z0-9]*(-[a-z0-9]+)*$");

    private static final Pattern ASCII_PATTERN =
            Pattern.compile("^[\\x00-\\x7F]*$");

    private static final Pattern HEX_COLOR_PATTERN =
            Pattern.compile("^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
            "^(?:" +
                    // 1. International code (optional)
                    "\\+\\d{1,4}[\\s\\-\\.]?" +
                    ")?" +

                    // 2. Area code (optional)
                    "(?:" +
                    "\\(?\\d{1,5}\\)?" +
                    "[\\s\\-\\.]?" +
                    ")?" +

                    // 3. Main number
                    "(?:" +
                    // Format A: XXX-XXX-XXXX veya XXX-XXXX (ABD)
                    "\\d{3}[\\s\\-\\.]?\\d{3}[\\s\\-\\.]?\\d{4}" +
                    "|" +
                    "\\d{3}[\\s\\-\\.]?\\d{4}" +
                    "|" +
                    // Format B: XXXX XXX XXXX (Türkiye)
                    "\\d{4}[\\s\\-\\.]?\\d{3}[\\s\\-\\.]?\\d{4}" +
                    "|" +
                    // Format C: XX XXXX XXXX veya XXX XXX XXXX (UK/Germany)
                    "\\d{2,3}[\\s\\-\\.]?\\d{3,4}[\\s\\-\\.]?\\d{3,4}" +
                    "|" +
                    // Format D: X XX XX XX XX (France)
                    "\\d{1,2}(?:[\\s\\-\\.]\\d{2}){3,4}" +
                    "|" +
                    // Format E: XXXXXX (6+ number)
                    "\\d{6,}" +
                    ")" +

                    // 4. Extension (opitonal)
                    "(?:\\s?(?:ext|x|ext\\.)\\s?\\d{1,5})?" +

                    "$"
    );

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://" +                                 // Protocol
                    "(?:" +
                    // IP address (v4)
                    "(?:\\d{1,3}\\.){3}\\d{1,3}" +
                    "|" +
                    // IPv6
                    "(?:\\[([0-9a-fA-F:]+)\\])" +
                    "|" +
                    // Domain name
                    "(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+" +
                    "[a-zA-Z]{2,}|" +
                    "localhost)" +
                    ")" +
                    "(?::\\d{1,5})?" +                                   // Port
                    "(?:/[^?#]*)?" +                                     // Path (query ve fragment olmadan)
                    "(?:\\?[^#]*)?" +                                    // Query string
                    "(?:#.*)?" +                                         // Fragment
                    "$", Pattern.CASE_INSENSITIVE
    );
}
