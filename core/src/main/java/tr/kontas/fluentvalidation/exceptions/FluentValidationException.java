package tr.kontas.fluentvalidation.exceptions;

import tr.kontas.fluentvalidation.validation.ValidationResult;
import tr.kontas.fluentvalidation.dtos.FieldError;

/**
 * Exception thrown when validation fails.
 * This exception contains detailed information about all validation errors.
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * {@code
 * try {
 *     validator.validateAndThrow(user);
 * } catch (FluentValidationException e) {
 *     System.out.println("Validation failed for " + e.getEntityName());
 *     for (FieldError error : e.getResult().getErrors()) {
 *         System.out.println(error.property() + ": " + error.message());
 *     }
 * }
 * }
 * </pre>
 *
 * @see ValidationResult
 * @see FieldError
 */
public class FluentValidationException extends RuntimeException {

    private final ValidationResult result;
    private final String entityName;

    /**
     * Creates a new FluentValidationException with validation results.
     *
     * @param result The validation result containing all errors
     * @param entityClass The class of the entity that failed validation
     */
    public FluentValidationException(ValidationResult result, Class<?> entityClass) {
        super(buildMessage(result, entityClass));
        this.result = result;
        this.entityName = entityClass.getSimpleName();
    }

    /**
     * Gets the validation result containing all error details.
     *
     * @return The validation result
     */
    public ValidationResult getResult() {
        return result;
    }

    /**
     * Gets the formatted error message.
     *
     * @return A formatted string containing all validation errors
     */
    @Override
    public String getMessage() {
        return buildMessage(result, entityName);
    }

    /**
     * Gets the name of the entity that failed validation.
     *
     * @return The entity name
     */
    public String getEntityName() {
        return this.entityName;
    }

    /**
     * Returns a string representation of the exception.
     *
     * @return A formatted string containing all validation errors
     */
    @Override
    public String toString() {
        return getMessage();
    }

    /**
     * Builds an error message from validation results and entity class.
     *
     * @param result The validation result
     * @param entityClass The entity class
     * @return A formatted error message
     */
    private static String buildMessage(ValidationResult result, Class<?> entityClass) {
        return buildMessage(result, entityClass.getSimpleName().toLowerCase());
    }

    /**
     * Builds an error message from validation results and entity name.
     *
     * @param result The validation result
     * @param entityName The entity name
     * @return A formatted error message
     */
    private static String buildMessage(ValidationResult result, String entityName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Validation failed for ").append(entityName).append("\n");
        for (FieldError e : result.getErrors()) {
            sb.append(" - ").append(e.property()).append(": ").append(e.message()).append("\n");
        }
        return sb.toString();
    }
}