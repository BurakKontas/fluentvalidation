package tr.kontas.fluentvalidation.validation;

import java.util.function.Predicate;

/**
 * Represents a single validation step with a predicate and error message.
 * This class encapsulates the logic for validating a single condition.
 *
 * @param <R> The type of value being validated
 */
public class ValidationStep<R> {

    private final Predicate<R> predicate;
    private String message;

    /**
     * Creates a new validation step.
     *
     * @param predicate The validation condition
     * @param message The error message if validation fails
     */
    public ValidationStep(Predicate<R> predicate, String message) {
        this.predicate = predicate;
        this.message = message;
    }

    /**
     * Validates a value against the predicate.
     *
     * @param value The value to validate
     * @return true if the value passes validation, false otherwise
     */
    public boolean isValid(R value) {
        return predicate.test(value);
    }

    /**
     * Gets the error message for this validation step.
     *
     * @return The error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message for this validation step.
     *
     * @param message The error message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
