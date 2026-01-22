package tr.kontas.fluentvalidation.spring.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import tr.kontas.fluentvalidation.Validate;
import tr.kontas.fluentvalidation.spring.FluentValidationEntityListener;
import tr.kontas.fluentvalidation.spring.example.validators.UserEntityValidator;

@Entity
@Table(name = "app_user")
@EntityListeners(FluentValidationEntityListener.class)
@Validate(validator = UserEntityValidator.class)
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private int age;
}
