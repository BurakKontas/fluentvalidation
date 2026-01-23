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
    <version>1.0.4</version>
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

## License

GPL-3.0 License