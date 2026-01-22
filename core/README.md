# FluentValidation Core for Java

A fluent validation library for Java inspired by .NET’s FluentValidation. It allows defining validation rules in a **readable**, **type-safe**, and **extensible** way for your domain objects.

## Features

* **Fluent API** – Chain validation rules for readability
* **Type-safe** – Compile-time safety using generics and lambda expressions
* **Custom Rules** – Easily extend with your own validators
* **Cascade Modes** – Control validation flow when rules fail
* **Conditional Validation (`skip`)** – Skip validation for specific objects

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

### 1. Define a model class

```java
@Data
public class User {
    private String name;
    private String email;
    private int age;
}
```

### 2. Create a validator

```java
public class UserValidator extends Validator<User> {

    public UserValidator() {
        ruleFor(User::getEmail)
            .notNull()
            .notEmpty()
            .email()
            .withMessage("Invalid email format");

        ruleFor(User::getAge)
            .greaterThan(18)
            .withMessage("Age must be over 18");

        ruleFor(User::getName)
            .notBlank()
            .minLength(3);
    }

    @Override
    public boolean skip(User user) {
        // Skip validation if email is "test@test.com"
        return "test@test.com".equalsIgnoreCase(user.getEmail());
    }
}
```

> The `skip()` method allows you to bypass all rules for a specific object. Return `true` to skip validation, or `false` to run validation normally.

### 3. Validate an object

```java
User user = new User();
user.setName("Jo");
user.setEmail("test@test.com"); // This will skip validation
user.setAge(16);

UserValidator validator = new UserValidator();
ValidationResult result = validator.validate(user);

if (!result.isValid()) {
    result.getErrors().forEach(error -> 
        System.out.println(error.getMessage())
    );
} else {
    System.out.println("Validation skipped or passed!");
}
```

## Available Validation Rules

| Rule             | Description                                |
| ---------------- | ------------------------------------------ |
| `notNull()`      | Value must not be null                     |
| `notEmpty()`     | String must not be empty                   |
| `notBlank()`     | String must not be blank (whitespace only) |
| `email()`        | Must be a valid email format               |
| `minLength(int)` | Minimum string length                      |
| `maxLength(int)` | Maximum string length                      |
| `greaterThan(T)` | Value must be greater than specified       |
| `lessThan(T)`    | Value must be less than specified          |

## Custom Messages

```java
ruleFor(User::getEmail)
    .email()
    .withMessage("Please provide a valid email address");
```

## Cascade Mode

```java
ruleFor(User::getEmail)
    .cascade(CascadeMode.STOP) // stops at first validation (works great if you're validating from database or external with high performance instances)
    .notNull()
    .email();
```

## License

GPL-3.0 License
