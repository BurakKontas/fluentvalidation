package tr.kontas.fluentvalidation.annotations;

import tr.kontas.fluentvalidation.validation.Validator;

import java.lang.annotation.*;

/**
 * Annotation to specify the validator class for a particular entity.
 * This annotation is used to link an entity class with its corresponding validator.
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * {@code
 * @Validate(UserValidator.class)
 * public class User {
 *     private String username;
 *     private String email;
 *     // ... getters and setters
 * }
 * }
 * </pre>
 *
 * @see Validator
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    /**
     * Specifies the validator class that should be used to validate the annotated class.
     *
     * @return The validator class
     */
    Class<? extends Validator<?>> validator();
}