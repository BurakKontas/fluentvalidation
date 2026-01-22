package fluentvalidation;

import tr.kontas.fluentvalidation.validation.Validator;

public class UserValidator extends Validator<User> {
    public UserValidator() {
        ruleFor(User::getEmail)
                .notNull()
                .notEmpty()
                .email()
                .withMessage("Email format is invalid");

        ruleFor(User::getAge)
                .greaterThan(18)
                .withMessage("Age should be over 18");

        ruleFor(User::getName)
                .notBlank()
                .minLength(3);
    }
}
