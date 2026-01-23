# FluentValidation Spring for Java

Spring integration for **FluentValidation Core**, providing **aspect-oriented validation** and **JPA entity validation** out-of-the-box.

---

## Features

* **Spring AOP Validation** – Automatically validate method arguments with `@Validated`
* **JPA Entity Validation** – Validate entities before `@PrePersist` and `@PreUpdate`
* **Conditional Beans** – Enable/disable integration via properties
* **Spring Context Support** – Access validators as Spring beans
* **Conditional Validation (`skip`)** – Skip validation dynamically

---

## Installation

Add the Spring integration dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>tr.kontas.fluentvalidation</groupId>
    <artifactId>fluentvalidation-spring</artifactId>
    <version>1.0.6</version>
</dependency>
```

> Make sure you also include the core module:

```xml
<dependency>
    <groupId>tr.kontas.fluentvalidation</groupId>
    <artifactId>fluentvalidation-core</artifactId>
    <version>1.0.6</version>
</dependency>
```

---

## Configuration

### Enable or disable FluentValidation Spring:

```properties
# Enable/disable all Spring validation
tr.kontas.fluentvalidation.spring.enabled=true

# Enable/disable JPA entity validation
tr.kontas.fluentvalidation.jpa.enabled=false
```

> If JPA integration is disabled, entity listeners will skip validation automatically.

---

## Quick Start

### 1. Create a JPA entity

```java
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
```

---

### 2. Create a Spring validator

```java
@Component
public class UserEntityValidator extends Validator<User> {

    public UserEntityValidator() {
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

### 3. JPA entity validation example

If JPA integration is enabled, the listener will validate entities before persistence:

```java
User user = new User();
user.setEmail("test@test.com"); // Will skip validation
user.setAge(16);

repository.save(user); // Validation will run unless skip() returns true
```

---

## Conditional Validation (`skip()`)

* Use `skip()` in your Spring validators to **dynamically bypass validation** based on object state.
* Example:

```java
@Override
public boolean skip(User user) {
    return "test@test.com".equalsIgnoreCase(user.getEmail());
}
```

* Return `true` → skip all rules for this object
* Return `false` → validate normally

---

## License

GPL-3.0 License
