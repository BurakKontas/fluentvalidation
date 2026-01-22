package tr.kontas.fluentvalidation.spring.example.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.Validate;
import tr.kontas.fluentvalidation.spring.example.validators.CreateUserRequestValidator;

@Data
@Validate(validator = CreateUserRequestValidator.class)
public class CreateUserRequest {
    private String email;
    private int age;
}
