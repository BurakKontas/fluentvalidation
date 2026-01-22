package tr.kontas.fluentvalidation.dtos;

import java.util.Objects;

public class PasswordMessages {
    private String minLength;
    private String maxLength;
    private String uppercase;
    private String lowercase;
    private String digit;
    private String specialChar;

    public PasswordMessages() {
    }

    public PasswordMessages(
            String minLength,
            String maxLength,
            String uppercase,
            String lowercase,
            String digit,
            String specialChar
    ) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.uppercase = uppercase;
        this.lowercase = lowercase;
        this.digit = digit;
        this.specialChar = specialChar;
    }

    public static PasswordMessages defaults() {
        return new PasswordMessages(
                "Password must be at least {min} characters long",
                "Password must be at maximum {max} characters long",
                "Password must contain at least one uppercase letter",
                "Password must contain at least one lowercase letter",
                "Password must contain at least one digit",
                "Password must contain at least one special character"
        );
    }

    public String getMinLength() {
        return minLength;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public String getUppercase() {
        return uppercase;
    }

    public String getLowercase() {
        return lowercase;
    }

    public String getDigit() {
        return digit;
    }

    public String getSpecialChar() {
        return specialChar;
    }

    public PasswordMessages setMinLength(String minLength) {
        this.minLength = minLength;
        return this;
    }

    public PasswordMessages setMaxLength(String maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public PasswordMessages setUppercase(String uppercase) {
        this.uppercase = uppercase;
        return this;
    }

    public PasswordMessages setLowercase(String lowercase) {
        this.lowercase = lowercase;
        return this;
    }

    public PasswordMessages setDigit(String digit) {
        this.digit = digit;
        return this;
    }

    public PasswordMessages setSpecialChar(String specialChar) {
        this.specialChar = specialChar;
        return this;
    }

    public String minLength(int min) {
        return replace(minLength, "{min}", min);
    }

    public String maxLength(int max) {
        return replace(maxLength, "{max}", max);
    }

    public String uppercase() {
        return uppercase;
    }

    public String lowercase() {
        return lowercase;
    }

    public String digit() {
        return digit;
    }

    public String specialChar() {
        return specialChar;
    }

    private String replace(String template, String key, Object value) {
        Objects.requireNonNull(template, "Message template must not be null");
        return template.replace(key, String.valueOf(value));
    }
}