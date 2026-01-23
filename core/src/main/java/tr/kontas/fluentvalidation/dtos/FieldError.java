package tr.kontas.fluentvalidation.dtos;

/**
 * Represents a validation error for a specific property.
 * This record is used to store information about validation failures.
 *
 * <p><b>Example:</b></p>
 * <pre>
 * {@code
 * FieldError error = new FieldError("username", "Username cannot be empty");
 * System.out.println(error.property()); // "username"
 * System.out.println(error.message());  // "Username cannot be empty"
 * }
 * </pre>
 *
 * @param property The name of the property that failed validation
 * @param message The error message describing the validation failure
 */
public record FieldError(String property, String message) {}