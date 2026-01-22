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
    <groupId>tr.kontas.fluentvalidation</groupId>
    <artifactId>fluentvalidation-core</artifactId>
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

| Rule                                 | Description                                 | Example Usage                                                                       |
| ------------------------------------ | ------------------------------------------- | ----------------------------------------------------------------------------------- |
| `notNull()`                          | Value must not be null                      | `ruleFor(User::getEmail).notNull();`                                                |
| `notEmpty()`                         | String must not be empty (`""`)             | `ruleFor(User::getName).notEmpty();`                                                |
| `notBlank()`                         | String must not be blank or whitespace only | `ruleFor(User::getName).notBlank();`                                                |
| `email()`                            | Must be a valid email format                | `ruleFor(User::getEmail).email();`                                                  |
| `minLength(int)`                     | Minimum string length                       | `ruleFor(User::getName).minLength(3);`                                              |
| `maxLength(int)`                     | Maximum string length                       | `ruleFor(User::getName).maxLength(50);`                                             |
| `greaterThan(Number)`                | Must be greater than the specified number   | `ruleFor(User::getAge).greaterThan(18);`                                            |
| `lessThan(Number)`                   | Must be less than the specified number      | `ruleFor(User::getAge).lessThan(100);`                                              |
| `equalTo(T)`                         | Must be equal to the specified value        | `ruleFor(User::getRole).equalTo("ADMIN");`                                          |
| `matches(String regex)`              | Must match the specified regex string       | `ruleFor(User::phone).matches("\\d{10}");`                                          |
| `matches(Pattern pattern)`           | Must match the specified `Pattern`          | `ruleFor(User::phone).matches(Pattern.compile("\\d{10}"));`                         |
| `must(Predicate<R>, String message)` | Custom validation rule                      | `ruleFor(User::age).must(a -> a % 2 == 0, "Age must be even");`                     |
| `withMessage(String)`                | Custom error message for the previous rule  | `ruleFor(User::email).email().withMessage("Please provide a valid email address");` |
| `cascade(CascadeMode)`               | Controls validation flow when a rule fails  | `ruleFor(User::email).cascade(CascadeMode.STOP).notNull().email();`                 |


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
    .cascade(CascadeMode.STOP)
    .notNull()
    .email();
```

## Annotation-Based Validation

Check FluentValidation AspectJ for native, FluentValidation Spring for Spring Boot applications.

## License

GPL-3.0 License
