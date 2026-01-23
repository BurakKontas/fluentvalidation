# FluentValidation AspectJ for Java

AspectJ integration for **FluentValidation Core**, allowing **automatic validation of objects** in native Java applications without Spring.

---

## Features

* **Aspect-Oriented Validation** – Automatically validate objects after construction
* **Native Java Support** – Works in plain Java projects, no Spring required
* **Dynamic Validation** – Uses `@Validate` annotation to associate validators
* **Conditional Validation (`skip()`)** – Skip validation dynamically in validators

---

## Installation

Add the **core** and **AspectJ** modules to your project:

```xml
<dependency>
    <groupId>tr.kontas.fluentvalidation</groupId>
    <artifactId>fluentvalidation-core</artifactId>
    <version>1.0.0</version>
</dependency>

<dependency>
    <groupId>tr.kontas.fluentvalidation</groupId>
    <artifactId>fluentvalidation-aspectj</artifactId>
    <version>1.0.0</version>
</dependency>
```

> Make sure AspectJ runtime (`aspectjrt`) is included. If using compile-time weaving, include `aspectjtools` in your build process.

---

## Quick Start

### 1. Annotate your class

Use `@Validate` to associate a validator:

```java
@Validate(validator = UserValidator.class)
public class User {
    private String email;
    private int age;

    public User(String email, int age) {
        this.email = email;
        this.age = age;
    }

    // getters and setters
}
```

---

### 2. Create a validator

```java
public class UserValidator extends Validator<User> {

    public UserValidator() {
        ruleFor(User::getEmail)
            .notBlank()
            .email()
            .withMessage("Email format is invalid");

        ruleFor(User::getAge)
            .greaterThan(18)
            .withMessage("Age should be over 18");
    }

    @Override
    public boolean skip(User user) {
        // Skip validation if email is "test@test.com"
        return "test@test.com".equalsIgnoreCase(user.getEmail());
    }
}
```

---

### 3. Example usage

```java
public class Main {
    public static void main(String[] args) {
        try {
            User user = new User("invalid-email", 16); // Will throw exception
        } catch (FluentValidationException ex) {
            ex.getErrors().forEach(error -> 
                System.out.println(error.getMessage())
            );
        }

        User skipUser = new User("test@test.com", 10); // Validation skipped
        System.out.println("Validation skipped for: " + skipUser.getEmail());
    }
}
```

---

## Key Points

* Works **without Spring**
* Uses **constructor interception** via AspectJ
* Validators are **instantiated dynamically** for each object
* `skip()` allows conditional validation based on object state

---

## License

GPL-3.0 License

