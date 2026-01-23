package tr.kontas.fluentvalidation.validation;

import tr.kontas.fluentvalidation.rule.RuleBuilder;
import tr.kontas.fluentvalidation.interfaces.SerializableFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for creating validators.
 * Extend this class to create custom validators for your entities.
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * {@code
 * public class UserValidator extends Validator<User> {
 *     public UserValidator() {
 *         ruleFor(User::getUsername)
 *             .notNull()
 *             .notEmpty()
 *             .minLength(3)
 *             .maxLength(50);
 *
 *         ruleFor(User::getEmail)
 *             .notNull()
 *             .email();
 *     }
 * }
 * }
 * </pre>
 *
 * @param <T> The type of object being validated
 */
public abstract class Validator<T> {

    private final List<RuleBuilder<T, ?>> rules = new ArrayList<>();

    /**
     * Creates a validation rule for a specific property.
     * This method uses method references to determine property names automatically.
     *
     * <p><b>Example:</b></p>
     * <pre>
     * {@code
     * ruleFor(User::getUsername) // Automatically resolves property name as "username"
     *     .notNull()
     *     .minLength(3);
     * }
     * </pre>
     *
     * @param <R> The type of the property
     * @param property A method reference to the property getter
     * @return A RuleBuilder for configuring validation rules
     */
    protected <R> RuleBuilder<T, R> ruleFor(SerializableFunction<T, R> property) {
        String propertyName = resolvePropertyName(property);
        RuleBuilder<T, R> rule = new RuleBuilder<>(propertyName, property);
        rules.add(rule);
        return rule;
    }

    /**
     * Validates an object against all defined rules.
     *
     * <p><b>Example:</b></p>
     * <pre>
     * {@code
     * UserValidator validator = new UserValidator();
     * ValidationResult result = validator.validate(user);
     *
     * if (result.isValid()) {
     *     // Proceed with business logic
     * } else {
     *     // Handle validation errors
     * }
     * }
     * </pre>
     *
     * @param target The object to validate
     * @return A ValidationResult containing all validation errors
     */
    public ValidationResult validate(T target) {
        ValidationResult result = new ValidationResult();
        if(skip(target)) return result;

        for (RuleBuilder<T, ?> rule : rules) {
            rule.validate(target, result);
        }
        return result;
    }

    /**
     * Resolves the property name from a method reference using Java's SerializedLambda.
     *
     * @param fn The method reference
     * @return The resolved property name
     * @throws RuntimeException if property name cannot be resolved
     */
    private String resolvePropertyName(SerializableFunction<T, ?> fn) {
        try {
            Method writeReplace = fn.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);

            SerializedLambda lambda =
                    (SerializedLambda) writeReplace.invoke(fn);

            String methodName = lambda.getImplMethodName();
            return extractPropertyName(methodName);

        } catch (Exception e) {
            throw new RuntimeException("Property name çözümlenemedi", e);
        }
    }

    /**
     * Extracts the property name from a getter method name.
     *
     * @param methodName The method name
     * @return The extracted property name
     */
    private String extractPropertyName(String methodName) {
        if (methodName.startsWith("get")) {
            return decapitalize(methodName.substring(3));
        }
        if (methodName.startsWith("is")) {
            return decapitalize(methodName.substring(2));
        }

        // record / custom accessor fallback
        return methodName;
    }

    /**
     * Converts the first character of a string to lowercase.
     *
     * @param value The input string
     * @return The string with first character lowercase
     */
    private String decapitalize(String value) {
        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }

    /**
     * Determines whether validation should be skipped for an entity.
     * Override this method to implement conditional validation.
     *
     * <p><b>Example:</b></p>
     * <pre>
     * {@code
     * @Override
     * public boolean skip(T entity) {
     *     // Skip validation for admin users
     *     return entity.isAdmin();
     * }
     * }
     * </pre>
     *
     * @param entity The entity to potentially skip validation for
     * @return true if validation should be skipped, false otherwise
     */
    public boolean skip(T entity) {
        return false;
    }
}