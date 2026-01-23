package tr.kontas.fluentvalidation.dtos;

import java.util.Objects;

/**
 * Provides customizable messages for password validation rules.
 * This class allows you to override default password validation messages.
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * {@code
 * PasswordMessages customMessages = new PasswordMessages()
 *     .setMinLength("Password must be at least {min} characters")
 *     .setUppercase("Include at least one uppercase letter");
 *
 * String message = customMessages.minLength(8); // "Password must be at least 8 characters"
 * }
 * </pre>
 */
public class PasswordMessages {
    private String minLength;
    private String maxLength;
    private String uppercase;
    private String lowercase;
    private String digit;
    private String specialChar;

    /**
     * Creates a new PasswordMessages instance with null values for all messages.
     */
    public PasswordMessages() {
    }

    /**
     * Creates a new PasswordMessages instance with specified messages.
     *
     * @param minLength Message for minimum length validation
     * @param maxLength Message for maximum length validation
     * @param uppercase Message for uppercase character requirement
     * @param lowercase Message for lowercase character requirement
     * @param digit Message for digit requirement
     * @param specialChar Message for special character requirement
     */
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

    /**
     * Returns the default password validation messages.
     *
     * @return A PasswordMessages instance with default messages
     */
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

    /**
     * Gets the minimum length message template.
     *
     * @return The minimum length message template
     */
    public String getMinLength() {
        return minLength;
    }

    /**
     * Gets the maximum length message template.
     *
     * @return The maximum length message template
     */
    public String getMaxLength() {
        return maxLength;
    }

    /**
     * Gets the uppercase requirement message.
     *
     * @return The uppercase requirement message
     */
    public String getUppercase() {
        return uppercase;
    }

    /**
     * Gets the lowercase requirement message.
     *
     * @return The lowercase requirement message
     */
    public String getLowercase() {
        return lowercase;
    }

    /**
     * Gets the digit requirement message.
     *
     * @return The digit requirement message
     */
    public String getDigit() {
        return digit;
    }

    /**
     * Gets the special character requirement message.
     *
     * @return The special character requirement message
     */
    public String getSpecialChar() {
        return specialChar;
    }

    /**
     * Sets the minimum length message template.
     *
     * @param minLength The message template (use {min} as placeholder)
     * @return This PasswordMessages instance for method chaining
     */
    public PasswordMessages setMinLength(String minLength) {
        this.minLength = minLength;
        return this;
    }

    /**
     * Sets the maximum length message template.
     *
     * @param maxLength The message template (use {max} as placeholder)
     * @return This PasswordMessages instance for method chaining
     */
    public PasswordMessages setMaxLength(String maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    /**
     * Sets the uppercase requirement message.
     *
     * @param uppercase The uppercase requirement message
     * @return This PasswordMessages instance for method chaining
     */
    public PasswordMessages setUppercase(String uppercase) {
        this.uppercase = uppercase;
        return this;
    }

    /**
     * Sets the lowercase requirement message.
     *
     * @param lowercase The lowercase requirement message
     * @return This PasswordMessages instance for method chaining
     */
    public PasswordMessages setLowercase(String lowercase) {
        this.lowercase = lowercase;
        return this;
    }

    /**
     * Sets the digit requirement message.
     *
     * @param digit The digit requirement message
     * @return This PasswordMessages instance for method chaining
     */
    public PasswordMessages setDigit(String digit) {
        this.digit = digit;
        return this;
    }

    /**
     * Sets the special character requirement message.
     *
     * @param specialChar The special character requirement message
     * @return This PasswordMessages instance for method chaining
     */
    public PasswordMessages setSpecialChar(String specialChar) {
        this.specialChar = specialChar;
        return this;
    }

    /**
     * Gets the formatted minimum length message with the minimum value inserted.
     *
     * @param min The minimum length value
     * @return The formatted minimum length message
     */
    public String minLength(int min) {
        return replace(minLength, "{min}", min);
    }

    /**
     * Gets the formatted maximum length message with the maximum value inserted.
     *
     * @param max The maximum length value
     * @return The formatted maximum length message
     */
    public String maxLength(int max) {
        return replace(maxLength, "{max}", max);
    }

    /**
     * Gets the uppercase requirement message.
     *
     * @return The uppercase requirement message
     */
    public String uppercase() {
        return uppercase;
    }

    /**
     * Gets the lowercase requirement message.
     *
     * @return The lowercase requirement message
     */
    public String lowercase() {
        return lowercase;
    }

    /**
     * Gets the digit requirement message.
     *
     * @return The digit requirement message
     */
    public String digit() {
        return digit;
    }

    /**
     * Gets the special character requirement message.
     *
     * @return The special character requirement message
     */
    public String specialChar() {
        return specialChar;
    }

    /**
     * Replaces a placeholder in a template string with a value.
     *
     * @param template The template string
     * @param key The placeholder to replace
     * @param value The value to insert
     * @return The formatted string
     */
    private String replace(String template, String key, Object value) {
        Objects.requireNonNull(template, "Message template must not be null");
        return template.replace(key, String.valueOf(value));
    }
}