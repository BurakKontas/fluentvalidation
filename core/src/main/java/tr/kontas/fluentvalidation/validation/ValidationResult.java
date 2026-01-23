package tr.kontas.fluentvalidation.validation;

import tr.kontas.fluentvalidation.dtos.FieldError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the result of a validation operation.
 * This class collects all validation errors and provides methods to check validation status.
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * {@code
 * ValidationResult result = validator.validate(user);
 * if (result.isValid()) {
 *     System.out.println("Validation passed!");
 * } else {
 *     for (FieldError error : result.getErrors()) {
 *         System.out.println(error.property() + ": " + error.message());
 *     }
 * }
 * }
 * </pre>
 *
 * @see FieldError
 */
public class ValidationResult {

    private final List<FieldError> errors = new ArrayList<>();

    /**
     * Adds a validation error to the result.
     *
     * @param property The name of the property that failed validation
     * @param message The error message describing the validation failure
     */
    public void addError(String property, String message) {
        errors.add(new FieldError(property, message));
    }

    /**
     * Checks if the validation passed (no errors).
     *
     * @return true if there are no validation errors, false otherwise
     */
    public boolean isValid() {
        return errors.isEmpty();
    }

    /**
     * Checks if the validation failed (has errors).
     *
     * @return true if there are validation errors, false otherwise
     */
    public boolean isNotValid() {
        return !isValid();
    }

    /**
     * Gets an unmodifiable list of all validation errors.
     *
     * @return List of validation errors
     */
    public List<FieldError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Returns a string representation of the validation result.
     *
     * @return A formatted string showing validation status and errors
     */
    @Override
    public String toString() {
        if (isValid()) {
            return "ValidationResult: valid";
        }

        return "ValidationResult: invalid\n" +
                errors.stream()
                        .map(e -> " - %s: %s".formatted(
                                e.property(),
                                e.message()
                        ))
                        .collect(Collectors.joining("\n"));
    }
}