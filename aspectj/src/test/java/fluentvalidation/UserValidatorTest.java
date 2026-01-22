package fluentvalidation;

import org.junit.jupiter.api.Test;
import tr.kontas.fluentvalidation.exceptions.FluentValidationException;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    void should_return_errors_when_user_is_invalid() {
        // given
        assertThrows(FluentValidationException.class, () -> new User("kontas", 10, "test@test.com"));
    }

    @Test
    void should_be_valid_when_user_is_valid() {
        // when
        assertDoesNotThrow(() -> new User("java", 60, "java@java.com"));
    }
}
