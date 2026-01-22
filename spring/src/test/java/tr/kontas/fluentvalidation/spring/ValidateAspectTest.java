package tr.kontas.fluentvalidation.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tr.kontas.fluentvalidation.FluentValidationException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class ValidateAspectTest {

    @Test
    void should_throw_exception_when_validation_fails() {

        FluentValidationException ex =
                assertThrows(
                        FluentValidationException.class,
                        () -> new TestUser("invalid-email")
                );

        assertNotNull(ex.getResult());
        assertTrue(ex.getMessage().contains("email"));
    }

    @Test
    void should_not_throw_exception_when_validation_passes() {

        assertDoesNotThrow(
                () -> new TestUser("test@test.com")
        );
    }
}
