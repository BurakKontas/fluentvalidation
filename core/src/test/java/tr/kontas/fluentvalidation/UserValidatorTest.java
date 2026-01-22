package tr.kontas.fluentvalidation;

import org.junit.jupiter.api.Test;
import tr.kontas.fluentvalidation.validation.ValidationResult;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private final UserValidator validator = new UserValidator();

    @Test
    void should_return_errors_when_user_is_invalid() {
        // given
        User user = new User("kontas", 10, "test@test.com");

        // when
        ValidationResult result = validator.validate(user);

        // then
        assertTrue(result.isNotValid());
        assertFalse(result.getErrors().isEmpty());
    }

    @Test
    void should_be_valid_when_user_is_valid() {
        // given
        User user = new User("java", 60, "java@java.com");

        // when
        ValidationResult result = validator.validate(user);

        // then
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void should_print_validation_result_string() {
        // given
        User user = new User("kontas", 10, "test@test.com");

        // when
        ValidationResult result = validator.validate(user);

        // then
        String text = result.toString();

        assertNotNull(text);
        assertTrue(text.contains("ValidationResult"));
    }
}
