# FluentValidation for Java

A fluent validation library for Java inspired by the .NET FluentValidation library. It provides a simple, readable, and type-safe way to define validation rules for your domain objects.

## Features

- **Fluent API** - Chain validation rules in a readable manner
- **Type-safe** - Uses generics and lambda expressions for compile-time safety
- **Extensible** - Easy to add custom validation rules
- **Cascade modes** - Control validation flow when rules fail
- **AOP Support** - Aspect-oriented validation with annotations

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>tr.kontas</groupId>
    <artifactId>fluentvalidation</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

### 1. Create a model class

```java
public class User {
    private String name;
    private String email;
    private int age;
    
    // getters and setters
}
```

### 2. Define a validator

```java
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
```

### 3. Validate an object

```java
User user = new User();
user.setName("Jo");
user.setEmail("invalid-email");
user.setAge(16);

UserValidator validator = new UserValidator();
ValidationResult result = validator.validate(user);

if (!result.isValid()) {
    result.getErrors().forEach(error -> 
        System.out.println(error.getMessage())
    );
}
```

## Available Validation Rules

| Rule | Description |
|------|-------------|
| `notNull()` | Value must not be null |
| `notEmpty()` | String must not be empty |
| `notBlank()` | String must not be blank (whitespace only) |
| `email()` | Must be a valid email format |
| `minLength(int)` | Minimum string length |
| `maxLength(int)` | Maximum string length |
| `greaterThan(T)` | Value must be greater than specified |
| `lessThan(T)` | Value must be less than specified |

## Custom Messages

Use `withMessage()` to specify custom error messages:

```java
ruleFor(User::getEmail)
    .email()
    .withMessage("Please provide a valid email address");
```

## Cascade Mode

Control whether validation continues after a failure:

```java
ruleFor(User::getEmail)
    .cascade(CascadeMode.STOP_ON_FIRST_FAILURE)
    .notNull()
    .email();
```

## Annotation-Based Validation

Use the `@Validate` annotation with AOP support:

```java
@Validate
public void createUser(User user) {
    // user is automatically validated before method execution
}
```

## License

MIT License