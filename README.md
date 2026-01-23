# FluentValidation Core for Java

A fluent validation library for Java inspired by .NET’s FluentValidation. It allows defining validation rules in a **readable**, **type-safe**, and **extensible** way for your domain objects.

## Features

* **Fluent API** – Chain validation rules for readability
* **Type-safe** – Compile-time safety using generics and lambda expressions
* **Custom Rules** – Easily extend with your own validators
* **Cascade Modes** – Control validation flow when rules fail
* **Conditional Validation (`skip`)** – Skip validation for specific objects
* **Comprehensive Validators** – Over 100 validation methods covering various scenarios

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>tr.kontas.fluentvalidation</groupId>
    <artifactId>fluentvalidation-core</artifactId>
    <version>1.0.7</version>
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
---

### ValidationResult

`ValidationResult` represents the outcome of a validation operation.
It is returned by the `validate()` method and contains all validation errors collected during rule execution.

```java
ValidationResult result = validator.validate(user);
```

---

#### Purpose

* Collects **all validation errors** for an object
* Provides an easy way to check whether validation **passed or failed**
* Stores errors in a **field-based** structure
* Supports multiple errors per object

---

#### Checking Validation Status

```java
if (result.isValid()) {
    System.out.println("Validation passed!");
}
```

Or the inverse:

```java
if (result.isNotValid()) {
    System.out.println("Validation failed!");
}
```

---

#### Accessing Validation Errors

Each validation error is represented by a `FieldError`, which contains:

* `property` – the name of the validated field
* `message` – the validation error message

```java
result.getErrors().forEach(error -> {
    System.out.println(error.property() + ": " + error.message());
});
```

Example output:

```
email: Invalid email format
age: Age must be over 18
```

> `getErrors()` returns an **unmodifiable list**, ensuring immutability.

---

#### FieldError Structure

```java
public record FieldError(String property, String message) {}
```

This makes error handling simple and type-safe.

---

#### String Representation

`ValidationResult` provides a helpful `toString()` implementation for debugging and logging.

```java
System.out.println(result);
```

Output example:

```
ValidationResult: invalid
 - email: Invalid email format
 - age: Age must be over 18
```

If validation passes:

```
ValidationResult: valid
```

---

#### Behavior with `skip()`

If validation is skipped via the `skip()` method in a validator:

```java
@Override
public boolean skip(User user) {
    return "test@test.com".equalsIgnoreCase(user.getEmail());
}
```

Then:

* No rules are executed
* `ValidationResult.isValid()` returns `true`
* No errors are added

---

#### When to Use ValidationResult Directly

Use `ValidationResult` when you want:

* Manual control over validation flow
* Non-exception-based validation
* Aggregated error reporting
* API-friendly validation responses

For automatic validation with exceptions, see **Validatable Interface**.

---

## Validatable Interface

The `Validatable` interface enables **automatic validation** using the `@Validate` annotation.
The annotated validator is resolved and executed at runtime, eliminating manual instantiation.

### Usage

#### 1. Annotate your class

```java
@Validate(validator = LoginCommandValidator.class)
public class LoginCommand implements Validatable {
    private String token;
}
```

#### 2. Define the validator

```java
public class LoginCommandValidator extends Validator<LoginCommand> {
    public LoginCommandValidator() {
        ruleFor(LoginCommand::getToken)
            .notNull()
            .notEmpty();
    }
}
```

#### 3. Validate

```java
ValidationResult result = command.validate();        // throws FluentValidationException on failure
ValidationResult result = command.validate(false);   // returns ValidationResult without throwing
```

#### Notes

* Requires `@Validate` annotation
* Validator must have a public no-args constructor
* Validation is triggered via `validate()` or `validate(false)`

---

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

## Available Validations

The library provides over 100 validation methods organized into different categories:

### Basic Rules
| Method | Description | Example |
|--------|-------------|---------|
| `notNull()` | Property value must not be null | `ruleFor(User::getName).notNull()` |
| `isNull()` | Property value must be null | `ruleFor(User::getMiddleName).isNull()` |
| `notEmpty()` | Must not be empty (strings, collections, arrays, maps) | `ruleFor(User::getName).notEmpty()` |
| `isEmpty()` | Must be empty | `ruleFor(User::getTempField).isEmpty()` |
| `notBlank()` | String must not be blank (not null, empty, or whitespace) | `ruleFor(User::getName).notBlank()` |

### Comparison Rules
| Method | Description | Example |
|--------|-------------|---------|
| `equalTo(value)` | Must equal specified value | `ruleFor(User::getStatus).equalTo("ACTIVE")` |
| `notEqualTo(value)` | Must not equal specified value | `ruleFor(User::getUsername).notEqualTo("admin")` |

### Numeric Rules
| Method | Description | Example |
|--------|-------------|---------|
| `greaterThan(value)` | Must be greater than value | `ruleFor(User::getAge).greaterThan(18)` |
| `greaterThanOrEqualTo(value)` | Must be greater than or equal to value | `ruleFor(User::getAge).greaterThanOrEqualTo(18)` |
| `lessThan(value)` | Must be less than value | `ruleFor(User::getAge).lessThan(100)` |
| `lessThanOrEqualTo(value)` | Must be less than or equal to value | `ruleFor(User::getAge).lessThanOrEqualTo(100)` |
| `inclusiveBetween(min, max)` | Must be between min and max (inclusive) | `ruleFor(User::getAge).inclusiveBetween(18, 65)` |
| `exclusiveBetween(min, max)` | Must be between min and max (exclusive) | `ruleFor(Discount::getAmount).exclusiveBetween(0, 100)` |
| `isPositive()` | Must be positive | `ruleFor(Product::getPrice).isPositive()` |
| `isNegative()` | Must be negative | `ruleFor(Temperature::getChange).isNegative()` |
| `isZero()` | Must be zero | `ruleFor(Balance::getAmount).isZero()` |
| `isNotZero()` | Must not be zero | `ruleFor(Balance::getAmount).isNotZero()` |
| `precisionScale(precision, scale)` | BigDecimal precision/scale limits | `ruleFor(Invoice::getAmount).precisionScale(10, 2)` |
| `isDivisibleBy(divisor)` | Must be divisible by divisor | `ruleFor(Package::getQuantity).isDivisibleBy(12)` |
| `isEven()` | Must be even number | `ruleFor(Batch::getSize).isEven()` |
| `isOdd()` | Must be odd number | `ruleFor(Group::getSize).isOdd()` |
| `isPercentage()` | Must be valid percentage (0-100) | `ruleFor(Discount::getPercentage).isPercentage()` |

### String Rules
| Method | Description | Example |
|--------|-------------|---------|
| `length(exact)` | Exact string length | `ruleFor(User::getZipCode).length(5)` |
| `length(min, max)` | String length between min and max | `ruleFor(User::getPassword).length(8, 20)` |
| `minLength(length)` | Minimum string length | `ruleFor(User::getName).minLength(3)` |
| `maxLength(length)` | Maximum string length | `ruleFor(User::getUsername).maxLength(50)` |
| `isAlpha()` | Only alphabetic characters | `ruleFor(User::getFirstName).isAlpha()` |
| `isNumeric()` | Only numeric digits | `ruleFor(User::getZipCode).isNumeric()` |
| `isAlphanumeric()` | Only alphanumeric characters | `ruleFor(User::getUsername).isAlphanumeric()` |
| `isUpperCase()` | All uppercase characters | `ruleFor(User::getCountryCode).isUpperCase()` |
| `isLowerCase()` | All lowercase characters | `ruleFor(User::getUsername).isLowerCase()` |
| `isHexadecimal()` | Valid hexadecimal string | `ruleFor(Color::getHexCode).isHexadecimal()` |
| `isBase64()` | Valid Base64 encoded string | `ruleFor(Image::getBase64Data).isBase64()` |
| `isUuid()` | Valid UUID | `ruleFor(Document::getId).isUuid()` |
| `containsNoWhitespace()` | No whitespace characters | `ruleFor(User::getUsername).containsNoWhitespace()` |
| `startsWith(prefix)` | Starts with specified prefix | `ruleFor(Image::getUrl).startsWith("https://")` |
| `endsWith(suffix)` | Ends with specified suffix | `ruleFor(File::getName).endsWith(".pdf")` |
| `matches(regex)` | Matches regular expression | `ruleFor(User::getUsername).matches("^[a-z0-9_]{3,20}$")` |
| `containsOnly(allowedChars)` | Contains only specified characters | `ruleFor(Code::getValue).containsOnly("ABCDEF1234567890")` |
| `doesNotContainAny(forbiddenChars)` | Does not contain forbidden characters | `ruleFor(Input::getText).doesNotContainAny("<>")` |
| `hasMinWords(min)` | Minimum number of words | `ruleFor(Description::getText).hasMinWords(10)` |
| `hasMaxWords(max)` | Maximum number of words | `ruleFor(Description::getText).hasMaxWords(100)` |
| `isCamelCase()` | camelCase format | `ruleFor(Variable::getName).isCamelCase()` |
| `isPascalCase()` | PascalCase format | `ruleFor(Class::getName).isPascalCase()` |
| `isSnakeCase()` | snake_case format | `ruleFor(Database::getColumn).isSnakeCase()` |
| `isKebabCase()` | kebab-case format | `ruleFor(CssClass::getName).isKebabCase()` |
| `isASCII()` | Only ASCII characters | `ruleFor(Input::getText).isASCII()` |

### Collection Rules
| Method | Description | Example |
|--------|-------------|---------|
| `hasMinCount(min)` | Minimum number of items | `ruleFor(Order::getItems).hasMinCount(1)` |
| `hasMaxCount(max)` | Maximum number of items | `ruleFor(Order::getItems).hasMaxCount(10)` |
| `hasExactCount(count)` | Exact number of items | `ruleFor(PhoneNumber::getDigits).hasExactCount(10)` |
| `hasUniqueItems()` | All items are unique | `ruleFor(User::getRoles).hasUniqueItems()` |
| `contains(item)` | Contains specified item | `ruleFor(User::getRoles).contains("ADMIN")` |
| `doesNotContain(item)` | Does not contain specified item | `ruleFor(User::getUsername).doesNotContain("admin")` |
| `allMatch(predicate)` | All items match predicate | `ruleFor(Students::getGrades).allMatch(grade -> grade >= 50)` |
| `anyMatch(predicate)` | At least one item matches predicate | `ruleFor(Students::getGrades).anyMatch(grade -> grade >= 90)` |
| `noneMatch(predicate)` | No items match predicate | `ruleFor(Students::getGrades).noneMatch(grade -> grade < 0)` |

### Format & Standards Rules
| Method | Description | Example |
|--------|-------------|---------|
| `email()` | Valid email address | `ruleFor(User::getEmail).email()` |
| `url()` | Valid URL | `ruleFor(User::getWebsite).url()` |
| `isInEnum(enumClass)` | Valid enum value | `ruleFor(User::getStatus).isInEnum(UserStatus.class)` |
| `isIban()` | Valid IBAN | `ruleFor(BankAccount::getIban).isIban()` |
| `isBic()` | Valid BIC/SWIFT code | `ruleFor(BankAccount::getBic).isBic()` |
| `isIsbn()` | Valid ISBN | `ruleFor(Book::getIsbn).isIsbn()` |
| `isIssn()` | Valid ISSN | `ruleFor(Journal::getIssn).isIssn()` |
| `creditCard()` | Valid credit card number (Luhn check) | `ruleFor(Payment::getCardNumber).creditCard()` |

### Phone & Network Rules
| Method | Description | Example |
|--------|-------------|---------|
| `isPhoneNumber()` | Valid phone number | `ruleFor(Contact::getPhone).isPhoneNumber()` |
| `isIpAddress()` | Valid IPv4 or IPv6 address | `ruleFor(Server::getIpAddress).isIpAddress()` |
| `isIpv4()` | Valid IPv4 address | `ruleFor(Server::getIpAddress).isIpv4()` |
| `isIpv6()` | Valid IPv6 address | `ruleFor(Server::getIpAddress).isIpv6()` |
| `isMacAddress()` | Valid MAC address | `ruleFor(Device::getMacAddress).isMacAddress()` |

### Date & Time Rules
| Method | Description | Example |
|--------|-------------|---------|
| `isInPast()` | Date is in the past | `ruleFor(User::getBirthDate).isInPast()` |
| `isInFuture()` | Date is in the future | `ruleFor(Appointment::getDate).isInFuture()` |
| `isToday()` | Date is today | `ruleFor(Task::getDueDate).isToday()` |
| `minAge(years)` | Minimum age from date | `ruleFor(User::getBirthDate).minAge(18)` |
| `maxAge(years)` | Maximum age from date | `ruleFor(User::getBirthDate).maxAge(100)` |
| `isAfter(date)` | Date is after specified date | `ruleFor(Event::getStartDate).isAfter(LocalDate.now())` |
| `isBefore(date)` | Date is before specified date | `ruleFor(Event::getEndDate).isBefore(LocalDate.now().plusDays(30))` |
| `isBetweenDates(start, end)` | Date is between start and end | `ruleFor(Booking::getDate).isBetweenDates(startDate, endDate)` |
| `isWeekday()` | Date is a weekday (Mon-Fri) | `ruleFor(Meeting::getDate).isWeekday()` |
| `isWeekend()` | Date is a weekend (Sat-Sun) | `ruleFor(Event::getDate).isWeekend()` |

### Password Rules
| Method | Description | Example |
|--------|-------------|---------|
| `containsUppercase()` | Contains uppercase letter | `ruleFor(User::getPassword).containsUppercase()` |
| `containsLowercase()` | Contains lowercase letter | `ruleFor(User::getPassword).containsLowercase()` |
| `containsDigit()` | Contains digit | `ruleFor(User::getPassword).containsDigit()` |
| `containsSpecialChar()` | Contains special character | `ruleFor(User::getPassword).containsSpecialChar()` |
| `strongPassword(min, max)` | Strong password with all criteria | `ruleFor(User::getPassword).strongPassword(8, 20)` |

### Security Rules
| Method | Description | Example |
|--------|-------------|---------|
| `containsNoSqlInjection()` | No SQL injection patterns | `ruleFor(UserInput::getSearchTerm).containsNoSqlInjection()` |
| `containsNoXss()` | No XSS attack patterns | `ruleFor(UserInput::getComment).containsNoXss()` |
| `containsNoCommandInjection()` | No command injection patterns | `ruleFor(Input::getCommand).containsNoCommandInjection()` |
| `containsNoLdapInjection()` | No LDAP injection patterns | `ruleFor(Input::getLdapQuery).containsNoLdapInjection()` |

### Coordinate & Measurement Rules
| Method | Description | Example |
|--------|-------------|---------|
| `isLatitude()` | Valid latitude (-90 to 90) | `ruleFor(Location::getLatitude).isLatitude()` |
| `isLongitude()` | Valid longitude (-180 to 180) | `ruleFor(Location::getLongitude).isLongitude()` |
| `isPort()` | Valid port number (0-65535) | `ruleFor(Server::getPort).isPort()` |
| `isHexColor()` | Valid hex color code | `ruleFor(Theme::getColor).isHexColor()` |

### File Size Rules
| Method | Description | Example |
|--------|-------------|---------|
| `maxSizeInBytes(bytes)` | Maximum size in bytes | `ruleFor(File::getSize).maxSizeInBytes(10 * 1024 * 1024)` |
| `maxSizeInKB(kb)` | Maximum size in kilobytes | `ruleFor(File::getSize).maxSizeInKB(1024)` |
| `maxSizeInMB(mb)` | Maximum size in megabytes | `ruleFor(File::getSize).maxSizeInMB(10)` |
| `maxSizeInGB(gb)` | Maximum size in gigabytes | `ruleFor(File::getSize).maxSizeInGB(1)` |

### Boolean Rules
| Method | Description | Example |
|--------|-------------|---------|
| `isTrue()` | Must be true | `ruleFor(User::isAgreedToTerms).isTrue()` |
| `isFalse()` | Must be false | `ruleFor(User::isDeleted).isFalse()` |

### Advanced String Rules
| Method | Description | Example |
|--------|-------------|---------|
| `containsPattern(regex)` | Contains regex pattern | `ruleFor(Text::getContent).containsPattern("\\bimportant\\b")` |
| `doesNotMatchPattern(regex)` | Does not match regex pattern | `ruleFor(Username::getValue).doesNotMatchPattern(".*admin.*")` |

### Custom Validation
| Method | Description | Example |
|--------|-------------|---------|
| `must(predicate, message)` | Custom validation logic | `ruleFor(User::getAge).must(age -> age >= 18, "Must be adult")` |

## Custom Validation

Create custom validation rules using the `must()` method:

```java
ruleFor(User::getAge)
    .must(age -> age != null && age >= 18, "Must be at least 18 years old");

ruleFor(Order::getTotal)
    .must(total -> total != null && total.compareTo(BigDecimal.ZERO) > 0, 
          "Total must be positive");
```

## Conditional Validation

Add conditional logic to your validation rules using `when()` and `unless()` methods.

### Basic Usage

```java
ruleFor(User::getAge)
    .when(User::isActive)          // Only validate if user is active
    .greaterThanOrEqualTo(18)
    .withMessage("Active users must be 18 or older");

ruleFor(User::getMiddleName)
    .unless(User::hasMiddleName)   // Only validate if user doesn't have middle name
    .isNull()
    .withMessage("Middle name must be null if not provided");
```

### `when()` - Conditional Validation

The `when()` method ensures a validation rule only executes when the specified condition is `true`.

#### Syntax
```java
.when(Predicate<T> condition)
```

#### Examples

**Example 1: Business rule validation**
```java
ruleFor(Order::getShippingAddress)
    .when(Order::requiresShipping)   // Only validate if shipping is required
    .notNull()
    .withMessage("Shipping address is required");
```

**Example 2: Country-specific validation**
```java
ruleFor(User::getPhoneNumber)
    .when(user -> "US".equals(user.getCountry()))
    .matches("^\\+1\\d{10}$")
    .withMessage("US phone numbers must start with +1");
```

**Example 3: Multiple conditions**
```java
ruleFor(Product::getDiscountPrice)
    .when(product -> product.isOnSale() && product.hasDiscount())
    .lessThan(product -> product.getOriginalPrice())
    .withMessage("Discount price must be lower than original price");
```

### `unless()` - Skip Validation Conditionally

The `unless()` method ensures a validation rule only executes when the specified condition is `false` (skip validation when condition is `true`).

#### Syntax
```java
.unless(Predicate<T> condition)
```

#### Examples

**Example 1: Skip validation for specific cases**
```java
ruleFor(User::getMiddleName)
    .unless(User::isRequiredToHaveMiddleName)  // Skip if middle name is optional
    .isNull()
    .withMessage("Middle name should be null if optional");
```

**Example 2: Skip validation for administrators**
```java
ruleFor(User::getPassword)
    .unless(User::isAdmin)          // Skip password validation for admins
    .minLength(8)
    .withMessage("Password must be at least 8 characters");
```

**Example 3: Skip validation for specific statuses**
```java
ruleFor(Order::getTrackingNumber)
    .unless(order -> order.getStatus() == OrderStatus.CANCELLED)
    .notNull()
    .withMessage("Tracking number is required for active orders");
```

### Parameterless `unless()` - Always Skip

The parameterless `unless()` method completely disables the validation rule.

```java
ruleFor(User::getLegacyField)
    .unless()        // Never validate this field
    .notNull();      // This rule will never be executed

ruleFor(System::getDebugMode)
    .unless()        // Skip in production
    .isTrue();       // Only validated if unless() is not called
```

### Chaining Conditional Rules

You can chain multiple conditional rules together for complex scenarios.

#### Example: Complex business logic
```java
ruleFor(Employee::getOvertimeHours)
    .when(Employee::isFullTime)                    // Only for full-time employees
    .unless(Employee::isExempt)                    // Unless exempt from overtime
    .when(emp -> emp.getWeeklyHours() > 40)        // And only if over 40 hours
    .greaterThan(0)
    .withMessage("Overtime hours must be tracked for eligible employees");
```

### Real-World Examples

#### Example 1: E-commerce validation
```java
public class OrderValidator extends Validator<Order> {
    public OrderValidator() {
        // Payment validation
        ruleFor(Order::getPaymentMethod)
            .when(Order::isPaid)                   // Only validate if order is paid
            .notNull()
            .withMessage("Payment method is required for paid orders");

        // Shipping validation
        ruleFor(Order::getShippingAddress)
            .unless(Order::isDigitalProduct)       // Skip for digital products
            .notNull()
            .withMessage("Shipping address is required for physical products");

        // Discount validation
        ruleFor(Order::getDiscountCode)
            .when(order -> order.getTotal() > 100) // Only for orders over $100
            .matches("^SAVE\\d{3}$")
            .withMessage("Invalid discount code format");
    }
}
```

#### Example 2: User registration validation
```java
public class UserRegistrationValidator extends Validator<UserRegistration> {
    public UserRegistrationValidator() {
        // Email validation (always required)
        ruleFor(UserRegistration::getEmail)
            .notNull()
            .email()
            .withMessage("Valid email is required");

        // Phone validation (conditional)
        ruleFor(UserRegistration::getPhoneNumber)
            .when(UserRegistration::isPhoneVerificationRequired)
            .notNull()
            .isPhoneNumber()
            .withMessage("Phone number is required for verification");

        // Password validation (skip for social login)
        ruleFor(UserRegistration::getPassword)
            .unless(UserRegistration::isSocialLogin)
            .notNull()
            .minLength(8)
            .containsDigit()
            .withMessage("Password is required and must be at least 8 characters with a digit");

        // Age validation (country-specific)
        ruleFor(UserRegistration::getBirthDate)
            .when(user -> "US".equals(user.getCountry()))
            .minAge(21)
            .withMessage("You must be 21 or older in the US");
    }
}
```

#### Example 3: Financial transaction validation
```java
public class TransactionValidator extends Validator<Transaction> {
    public TransactionValidator() {
        // Amount validation
        ruleFor(Transaction::getAmount)
            .greaterThan(0)
            .withMessage("Amount must be positive");

        // Currency validation for international transfers
        ruleFor(Transaction::getCurrency)
            .when(Transaction::isInternational)
            .isInEnum(Currency.class)
            .withMessage("Valid currency is required for international transfers");

        // IBAN validation for bank transfers
        ruleFor(Transaction::getRecipientIban)
            .when(Transaction::isBankTransfer)
            .notNull()
            .isIban()
            .withMessage("Valid IBAN is required for bank transfers");

        // Skip validation for internal transfers
        ruleFor(Transaction::getRoutingNumber)
            .unless(Transaction::isInternalTransfer)
            .notNull()
            .withMessage("Routing number is required for external transfers");
    }
}
```

### Best Practices

1. **Use descriptive conditions**: Make conditions readable and self-explanatory
2. **Combine with cascade mode**: Use `cascade(CascadeMode.STOP)` with conditional rules for performance
3. **Keep conditions simple**: Avoid overly complex conditions in lambda expressions
4. **Test all scenarios**: Ensure both true and false conditions are tested
5. **Use method references**: When possible, use method references instead of lambda expressions for better readability

### Common Patterns

#### Pattern 1: Required field based on another field
```java
ruleFor(Form::getAlternateEmail)
    .when(form -> form.getPrimaryEmail() == null)
    .notNull()
    .email()
    .withMessage("Alternate email is required if primary email is not provided");
```

#### Pattern 2: Validation based on user role
```java
ruleFor(User::getSecurityLevel)
    .when(User::isAdministrator)
    .greaterThanOrEqualTo(3)
    .withMessage("Administrators must have security level 3 or higher");
```

#### Pattern 3: Date-based conditional validation
```java
ruleFor(Event::getEndDate)
    .when(event -> event.getStartDate() != null)
    .greaterThan(event -> event.getStartDate())
    .withMessage("End date must be after start date");
```

#### Pattern 4: Skip validation during specific operations
```java
public class ProductValidator extends Validator<Product> {
    @Override
    public boolean skip(Product product) {
        // Skip validation during import operations
        return product.isBeingImported();
    }
    
    public ProductValidator() {
        ruleFor(Product::getPrice)
            .unless()  // Will never run if skip() returns true
            .greaterThan(0);
    }
}
```

### Performance Considerations

1. **Condition evaluation order**: Conditions are evaluated before validation, minimizing unnecessary validations
2. **Predicate efficiency**: Use efficient predicates for conditions, especially with large datasets
3. **Cascade modes**: Combine with appropriate cascade modes to stop early when possible
4. **Lazy evaluation**: Conditions are evaluated lazily, only when needed

### Error Messages with Conditions

You can customize error messages for conditional validations:

```java
ruleFor(User::getAge)
    .when(User::isStudent)
    .inclusiveBetween(18, 25)
    .withMessage("Student age must be between 18 and 25");
```

Conditional validations provide powerful flexibility for implementing complex business rules while maintaining clean, readable validation logic.



## License

GPL-3.0 License