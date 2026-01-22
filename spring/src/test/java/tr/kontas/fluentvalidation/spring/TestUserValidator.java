package tr.kontas.fluentvalidation.spring;

import org.springframework.stereotype.Component;
import tr.kontas.fluentvalidation.Validator;

@Component
public class TestUserValidator extends Validator<TestUser> {

    public TestUserValidator() {
        ruleFor(TestUser::getEmail)
                .notNull()
                .email();
    }
}
