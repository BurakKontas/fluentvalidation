package tr.kontas.fluentvalidation.spring.example.validators;

import org.springframework.stereotype.Component;
import tr.kontas.fluentvalidation.Validator;
import tr.kontas.fluentvalidation.spring.example.entity.User;

@Component
public class UserEntityValidator extends Validator<User> {

    public UserEntityValidator() {
        ruleFor(User::getEmail)
                .notBlank()
                .email();

        ruleFor(User::getAge)
                .greaterThan(18);
    }

    @Override
    public boolean skip(User user) {
        return "test@test.com".equalsIgnoreCase(user.getEmail());
    }
}
