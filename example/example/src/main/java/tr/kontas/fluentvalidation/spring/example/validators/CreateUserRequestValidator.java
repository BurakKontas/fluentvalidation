package tr.kontas.fluentvalidation.spring.example.validators;

import org.springframework.stereotype.Component;
import tr.kontas.fluentvalidation.Validator;
import tr.kontas.fluentvalidation.spring.example.dtos.CreateUserRequest;

@Component
public class CreateUserRequestValidator extends Validator<CreateUserRequest> {

    public CreateUserRequestValidator() {

        ruleFor(CreateUserRequest::getEmail)
                .notBlank()
                .email()
                .withMessage("Invalid email");

        ruleFor(CreateUserRequest::getAge)
                .greaterThan(10)
                .withMessage("Age must be > 10");
    }
}