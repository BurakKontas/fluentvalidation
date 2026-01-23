package tr.kontas.fluentvalidation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tr.kontas.fluentvalidation.validation.ValidationResult;
import tr.kontas.fluentvalidation.validators.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RuleBuilderTest {

    // ========== BASIC RULES TESTS ==========
    @Nested
    class BasicRulesTests {

        @Test
        void notNull_should_pass_when_value_is_not_null() {
            NotNullValidator validator = new NotNullValidator();
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void notNull_should_fail_when_value_is_null() {
            NotNullValidator validator = new NotNullValidator();
            TestModel model = new TestModel(null);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());

            boolean hasErrorForValue = result.getErrors().stream()
                    .anyMatch(error -> error.property().equals("value"));
            assertTrue(hasErrorForValue, "Should have error for 'value' field");
        }

        @Test
        void isNull_should_pass_when_value_is_null() {
            IsNullValidator validator = new IsNullValidator();
            TestModel model = new TestModel(null);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isNull_should_fail_when_value_is_not_null() {
            IsNullValidator validator = new IsNullValidator();
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void notEmpty_should_pass_when_string_is_not_empty() {
            NotEmptyStringValidator validator = new NotEmptyStringValidator();
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void notEmpty_should_fail_when_string_is_empty() {
            NotEmptyStringValidator validator = new NotEmptyStringValidator();
            TestModel model = new TestModel("");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void notEmpty_should_fail_when_collection_is_empty() {
            NotEmptyCollectionValidator validator = new NotEmptyCollectionValidator();
            TestModel model = new TestModel("test");
            model.setList(new ArrayList<>());
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void isEmpty_should_pass_when_string_is_empty() {
            IsEmptyValidator validator = new IsEmptyValidator();
            TestModel model = new TestModel("");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void notBlank_should_pass_when_string_is_not_blank() {
            NotBlankValidator validator = new NotBlankValidator();
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void notBlank_should_fail_when_string_is_blank() {
            NotBlankValidator validator = new NotBlankValidator();
            TestModel model = new TestModel("   ");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }
    }

    // ========== COMPARISON RULES TESTS ==========

    @Nested
    class ComparisonRulesTests {

        @Test
        void equalTo_should_pass_when_values_are_equal() {
            EqualToValidator validator = new EqualToValidator("test");
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void equalTo_should_fail_when_values_are_not_equal() {
            EqualToValidator validator = new EqualToValidator("test");
            TestModel model = new TestModel("other");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void notEqualTo_should_pass_when_values_are_not_equal() {
            NotEqualToValidator validator = new NotEqualToValidator("test");
            TestModel model = new TestModel("other");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void notEqualTo_should_fail_when_values_are_equal() {
            NotEqualToValidator validator = new NotEqualToValidator("test");
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }
    }

    // ========== NUMERIC COMPARISON TESTS ==========

    @Nested
    class NumericComparisonTests {

        @Test
        void greaterThan_should_pass_when_value_is_greater() {
            GreaterThanValidator validator = new GreaterThanValidator(18);
            TestModel model = new TestModel("test");
            model.setAge(20);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void greaterThan_should_fail_when_value_is_not_greater() {
            GreaterThanValidator validator = new GreaterThanValidator(18);
            TestModel model = new TestModel("test");
            model.setAge(18);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void greaterThanOrEqualTo_should_pass_when_value_is_equal() {
            GreaterThanOrEqualValidator validator = new GreaterThanOrEqualValidator(18);
            TestModel model = new TestModel("test");
            model.setAge(18);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void lessThan_should_pass_when_value_is_less() {
            LessThanValidator validator = new LessThanValidator(100);
            TestModel model = new TestModel("test");
            model.setAge(50);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void lessThan_should_fail_when_value_is_not_less() {
            LessThanValidator validator = new LessThanValidator(100);
            TestModel model = new TestModel("test");
            model.setAge(100);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void lessThanOrEqualTo_should_pass_when_value_is_equal() {
            LessThanOrEqualValidator validator = new LessThanOrEqualValidator(100);
            TestModel model = new TestModel("test");
            model.setAge(100);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void inclusiveBetween_should_pass_when_value_is_in_range() {
            InclusiveBetweenValidator validator = new InclusiveBetweenValidator(18, 65);
            TestModel model = new TestModel("test");
            model.setAge(30);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void inclusiveBetween_should_pass_when_value_is_at_boundaries() {
            InclusiveBetweenValidator validator = new InclusiveBetweenValidator(18, 65);
            TestModel model = new TestModel("test");
            model.setAge(18);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void exclusiveBetween_should_pass_when_value_is_in_range() {
            ExclusiveBetweenValidator validator = new ExclusiveBetweenValidator(18, 65);
            TestModel model = new TestModel("test");
            model.setAge(30);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void exclusiveBetween_should_fail_when_value_is_at_boundaries() {
            ExclusiveBetweenValidator validator = new ExclusiveBetweenValidator(18, 65);
            TestModel model = new TestModel("test");
            model.setAge(18);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void isPositive_should_pass_when_value_is_positive() {
            IsPositiveValidator validator = new IsPositiveValidator();
            TestModel model = new TestModel("test");
            model.setAge(10);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isPositive_should_fail_when_value_is_zero() {
            IsPositiveValidator validator = new IsPositiveValidator();
            TestModel model = new TestModel("test");
            model.setAge(0);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void isNegative_should_pass_when_value_is_negative() {
            IsNegativeValidator validator = new IsNegativeValidator();
            TestModel model = new TestModel("test");
            model.setAge(-10);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isZero_should_pass_when_value_is_zero() {
            IsZeroValidator validator = new IsZeroValidator();
            TestModel model = new TestModel("test");
            model.setAge(0);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isNotZero_should_pass_when_value_is_not_zero() {
            IsNotZeroValidator validator = new IsNotZeroValidator();
            TestModel model = new TestModel("test");
            model.setAge(10);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }
    }

    // ========== DECIMAL PRECISION TESTS ==========

    @Nested
    class DecimalPrecisionTests {

        @Test
        void precisionScale_should_pass_when_within_limits() {
            PrecisionScaleValidator validator = new PrecisionScaleValidator(5, 2);
            TestModel model = new TestModel("test");
            model.setPrice(new BigDecimal("123.45"));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void precisionScale_should_fail_when_exceeds_limits() {
            PrecisionScaleValidator validator = new PrecisionScaleValidator(5, 2);
            TestModel model = new TestModel("test");
            model.setPrice(new BigDecimal("1234.567"));
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }
    }

    // ========== STRING LENGTH TESTS ==========

    @Nested
    class StringLengthTests {

        @Test
        void length_exact_should_pass_when_length_matches() {
            ExactLengthValidator validator = new ExactLengthValidator(4);
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void length_exact_should_fail_when_length_does_not_match() {
            ExactLengthValidator validator = new ExactLengthValidator(5);
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void length_range_should_pass_when_within_range() {
            LengthRangeValidator validator = new LengthRangeValidator(3, 10);
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void minLength_should_pass_when_length_is_sufficient() {
            MinLengthValidator validator = new MinLengthValidator(3);
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void minLength_should_fail_when_length_is_insufficient() {
            MinLengthValidator validator = new MinLengthValidator(10);
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void maxLength_should_pass_when_length_is_within_limit() {
            MaxLengthValidator validator = new MaxLengthValidator(10);
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void maxLength_should_fail_when_length_exceeds_limit() {
            MaxLengthValidator validator = new MaxLengthValidator(3);
            TestModel model = new TestModel("test");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }
    }

    // ========== COLLECTION TESTS ==========

    @Nested
    class CollectionTests {

        @Test
        void hasMinCount_should_pass_when_collection_has_enough_items() {
            HasMinCountValidator validator = new HasMinCountValidator(2);
            TestModel model = new TestModel("test");
            model.setList(Arrays.asList("a", "b", "c"));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void hasMinCount_should_fail_when_collection_has_insufficient_items() {
            HasMinCountValidator validator = new HasMinCountValidator(5);
            TestModel model = new TestModel("test");
            model.setList(Arrays.asList("a", "b", "c"));
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void hasMaxCount_should_pass_when_collection_is_within_limit() {
            HasMaxCountValidator validator = new HasMaxCountValidator(5);
            TestModel model = new TestModel("test");
            model.setList(Arrays.asList("a", "b", "c"));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void hasMaxCount_should_fail_when_collection_exceeds_limit() {
            HasMaxCountValidator validator = new HasMaxCountValidator(2);
            TestModel model = new TestModel("test");
            model.setList(Arrays.asList("a", "b", "c"));
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void hasExactCount_should_pass_when_count_matches() {
            HasExactCountValidator validator = new HasExactCountValidator(3);
            TestModel model = new TestModel("test");
            model.setList(Arrays.asList("a", "b", "c"));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void hasUniqueItems_should_pass_when_all_items_are_unique() {
            HasUniqueItemsValidator validator = new HasUniqueItemsValidator();
            TestModel model = new TestModel("test");
            model.setList(Arrays.asList("a", "b", "c"));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void hasUniqueItems_should_fail_when_duplicates_exist() {
            HasUniqueItemsValidator validator = new HasUniqueItemsValidator();
            TestModel model = new TestModel("test");
            model.setList(Arrays.asList("a", "b", "a"));
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void contains_should_pass_when_item_exists() {
            ContainsValidator validator = new ContainsValidator("b");
            TestModel model = new TestModel("test");
            model.setList(Arrays.asList("a", "b", "c"));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void doesNotContain_should_pass_when_item_does_not_exist() {
            DoesNotContainValidator validator = new DoesNotContainValidator("z");
            TestModel model = new TestModel("test");
            model.setList(Arrays.asList("a", "b", "c"));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }
    }

    // ========== ENUM TESTS ==========

    @Nested
    class EnumTests {

        @Test
        void isInEnum_should_pass_when_valid_enum_value() {
            IsInEnumValidator validator = new IsInEnumValidator();
            TestModel model = new TestModel("VALUE1");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isInEnum_should_fail_when_invalid_enum_value() {
            IsInEnumValidator validator = new IsInEnumValidator();
            TestModel model = new TestModel("INVALID");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }
    }

    // ========== STRING FORMAT TESTS ==========

    @Nested
    class StringFormatTests {

        static Stream<String> validEmailFormats() {
            return Stream.of(
                    "test@example.com",
                    "user.name@domain.com",
                    "user_name@domain.com",
                    "user-name@domain.com",
                    "user+tag@domain.com",
                    "user@sub.domain.com",
                    "user@domain.co.uk",
                    "user123@domain.com",
                    "UPPERCASE@DOMAIN.COM",
                    "a@b.cd"
            );
        }

        static Stream<String> invalidEmailFormats() {
            return Stream.of(
                    "invalid-email",
                    "@domain.com",
                    "user@",
                    "user@.com",
                    "user@domain.",
                    "user@domain..com",
                    "user@domain_com",
                    "user name@domain.com",
                    "user@domain.123",
                    "user@-domain.com"
            );
        }

        static Stream<String> validUrlFormats() {
            return Stream.of(
                    "https://www.example.com",
                    "http://example.com",
                    "http://example.com/path",
                    "http://example.com/path/to/file.html",
                    "http://example.com?query=param",
                    "http://example.com#fragment",
                    "http://sub.example.com",
                    "http://example.co.uk",
                    "http://192.168.1.1",
                    "http://localhost:8080"
            );
        }

        static Stream<String> invalidUrlFormats() {
            return Stream.of(
                    "not-a-url",
                    "http://",
                    "http://.com",
                    "http://example..com",
                    "http://example.com /path",
                    "://example.com"
            );
        }

        static Stream<String> validUuidFormats() {
            return Stream.of(
                    "123e4567-e89b-12d3-a456-426614174000",
                    "123E4567-E89B-12D3-A456-426614174000",
                    "123e4567e89b12d3a456426614174000",
                    "00000000-0000-0000-0000-000000000000"
            );
        }

        static Stream<String> invalidUuidFormats() {
            return Stream.of(
                    "not-a-uuid",
                    "123e4567-e89b-12d3-a456",
                    "123e4567-e89b-12d3-a456-426614174000-extra",
                    "123e4567-e89b-12d3-a456-42661417400g"
            );
        }

        static Stream<String> validBase64Formats() {
            return Stream.of(
                    "SGVsbG8gV29ybGQ=",
                    "SGVsbG8gV29ybGQ",
                    "SGVsbG8=",
                    "TWFueSBoYW5kcyBtYWtlIGxpZ2h0IHdvcmsu",
                    "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8gISIjJCUmJygpKissLS4vMDEyMzQ1Njc4OTo7PD0+P0BBQkNERUZHSElKS0xNTk9QUVJTVFVWV1hZWltcXV5fYGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6e3x9fn+AgYKDhIWGh4iJiouMjY6PkJGSk5SVlpeYmZqbnJ2en6ChoqOkpaanqKmqq6ytrq+wsbKztLW2t7i5uru8vb6/wMHCw8TFxsfIycrLzM3Oz9DR0tPU1dbX2Nna29zd3t/g4eLj5OXm5+jp6uvs7e7v8PHy8/T19vf4+fr7/P3+/w==",
                    "data:image/png;base64,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"
            );
        }

        static Stream<String> invalidBase64Formats() {
            return Stream.of(
                    "Not Base64!",
                    "SGVsbG8gV29ybGQ===",
                    "SGVsbG8=V29ybGQ=",
                    "SGVsbG8gV29ybGQ=!",
                    "SGVsbG8gV29ybGQ=="
            );
        }

        @ParameterizedTest
        @MethodSource("validEmailFormats")
        void email_should_pass_when_valid_email(String email) {
            EmailValidator validator = new EmailValidator();
            TestModel model = new TestModel(email);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for email: " + email);
        }

        @ParameterizedTest
        @MethodSource("invalidEmailFormats")
        void email_should_fail_when_invalid_email(String email) {
            EmailValidator validator = new EmailValidator();
            TestModel model = new TestModel(email);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for email: " + email);
        }

        @ParameterizedTest
        @MethodSource("validUrlFormats")
        void url_should_pass_when_valid_url(String url) {
            UrlValidator validator = new UrlValidator();
            TestModel model = new TestModel(url);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for URL: " + url);
        }

        @ParameterizedTest
        @MethodSource("invalidUrlFormats")
        void url_should_fail_when_invalid_url(String url) {
            UrlValidator validator = new UrlValidator();
            TestModel model = new TestModel(url);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for URL: " + url);
        }

        @Test
        void isAlpha_should_pass_when_only_letters() {
            IsAlphaValidator validator = new IsAlphaValidator();
            TestModel model = new TestModel("abcXYZ");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isAlpha_should_fail_when_contains_numbers() {
            IsAlphaValidator validator = new IsAlphaValidator();
            TestModel model = new TestModel("abc123");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void isNumeric_should_pass_when_only_digits() {
            IsNumericValidator validator = new IsNumericValidator();
            TestModel model = new TestModel("12345");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isNumeric_should_fail_when_contains_letters() {
            IsNumericValidator validator = new IsNumericValidator();
            TestModel model = new TestModel("123abc");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void isAlphanumeric_should_pass_when_letters_and_numbers() {
            IsAlphanumericValidator validator = new IsAlphanumericValidator();
            TestModel model = new TestModel("abc123");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isAlphanumeric_should_fail_when_contains_special_chars() {
            IsAlphanumericValidator validator = new IsAlphanumericValidator();
            TestModel model = new TestModel("abc-123");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void isUpperCase_should_pass_when_all_uppercase() {
            IsUpperCaseValidator validator = new IsUpperCaseValidator();
            TestModel model = new TestModel("HELLO");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isUpperCase_should_fail_when_contains_lowercase() {
            IsUpperCaseValidator validator = new IsUpperCaseValidator();
            TestModel model = new TestModel("Hello");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void isLowerCase_should_pass_when_all_lowercase() {
            IsLowerCaseValidator validator = new IsLowerCaseValidator();
            TestModel model = new TestModel("hello");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isHexadecimal_should_pass_when_valid_hex() {
            IsHexadecimalValidator validator = new IsHexadecimalValidator();
            TestModel model = new TestModel("#FF5733");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @ParameterizedTest
        @MethodSource("validBase64Formats")
        void isBase64_should_pass_when_valid_base64(String base64) {
            IsBase64Validator validator = new IsBase64Validator();
            TestModel model = new TestModel(base64);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for Base64: " + base64);
        }

        @ParameterizedTest
        @MethodSource("invalidBase64Formats")
        void isBase64_should_fail_when_invalid_base64(String base64) {
            IsBase64Validator validator = new IsBase64Validator();
            TestModel model = new TestModel(base64);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for Base64: " + base64);
        }

        @ParameterizedTest
        @MethodSource("validUuidFormats")
        void isUuid_should_pass_when_valid_uuid(String uuid) {
            IsUuidValidator validator = new IsUuidValidator();
            TestModel model = new TestModel(uuid);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for UUID: " + uuid);
        }

        @ParameterizedTest
        @MethodSource("invalidUuidFormats")
        void isUuid_should_fail_when_invalid_uuid(String uuid) {
            IsUuidValidator validator = new IsUuidValidator();
            TestModel model = new TestModel(uuid);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for UUID: " + uuid);
        }

        @Test
        void containsNoWhitespace_should_pass_when_no_whitespace() {
            ContainsNoWhitespaceValidator validator = new ContainsNoWhitespaceValidator();
            TestModel model = new TestModel("HelloWorld");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void containsNoWhitespace_should_fail_when_contains_whitespace() {
            ContainsNoWhitespaceValidator validator = new ContainsNoWhitespaceValidator();
            TestModel model = new TestModel("Hello World");
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void startsWith_should_pass_when_starts_with_prefix() {
            StartsWithValidator validator = new StartsWithValidator("Hello");
            TestModel model = new TestModel("HelloWorld");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void endsWith_should_pass_when_ends_with_suffix() {
            EndsWithValidator validator = new EndsWithValidator("World");
            TestModel model = new TestModel("HelloWorld");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }
    }

    // ========== INTERNATIONAL STANDARDS TESTS ==========

    @Nested
    class InternationalStandardsTests {

        static Stream<String> validIbanFormats() {
            return Stream.of(
                    "GB82WEST12345698765432",
                    "GB82 WEST 1234 5698 7654 32",
                    "TR330006100519786457841326",
                    "DE89370400440532013000",
                    "FR1420041010050500013M02606",
                    "IT60X0542811101000000123456"
            );
        }

        static Stream<String> invalidIbanFormats() {
            return Stream.of(
                    "INVALIDIBAN",
                    "GB82WEST123",
                    "GB82WEST123456987654321234567890",
                    "XX00000000000000000000"
            );
        }

        static Stream<String> validIsbnFormats() {
            return Stream.of(
                    "0-306-40615-2",
                    "0306406152",
                    "978-0-306-40615-7",
                    "9780306406157",
                    "978-3-16-148410-0",
                    "1-56619-909-3"
            );
        }

        static Stream<String> invalidIsbnFormats() {
            return Stream.of(
                    "INVALIDISBN",
                    "0-306-40615-3",
                    "978-0-306-40615-8",
                    "123456789",
                    "12345678901234"
            );
        }

        static Stream<String> validCreditCardFormats() {
            return Stream.of(
                    "4532015112830366",
                    "4532 0151 1283 0366",
                    "5532 0151 1283 0363",
                    "378282246310005",
                    "371449635398431",
                    "6011111111111117",
                    "30569309025904",
                    "3530111333300000"
            );
        }

        static Stream<String> invalidCreditCardFormats() {
            return Stream.of(
                    "1234567890123456",
                    "4532015112830367",
                    "1234",
                    "12345678901234567890"
            );
        }

        @ParameterizedTest
        @MethodSource("validIbanFormats")
        void isIban_should_pass_when_valid_iban(String iban) {
            IsIbanValidator validator = new IsIbanValidator();
            TestModel model = new TestModel(iban);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for IBAN: " + iban);
        }

        @ParameterizedTest
        @MethodSource("invalidIbanFormats")
        void isIban_should_fail_when_invalid_iban(String iban) {
            IsIbanValidator validator = new IsIbanValidator();
            TestModel model = new TestModel(iban);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for IBAN: " + iban);
        }

        @Test
        void isBic_should_pass_when_valid_bic() {
            IsBicValidator validator = new IsBicValidator();
            TestModel model = new TestModel("DEUTDEFF");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @ParameterizedTest
        @MethodSource("validIsbnFormats")
        void isIsbn_should_pass_when_valid_isbn(String isbn) {
            IsIsbnValidator validator = new IsIsbnValidator();
            TestModel model = new TestModel(isbn);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for ISBN: " + isbn);
        }

        @ParameterizedTest
        @MethodSource("invalidIsbnFormats")
        void isIsbn_should_fail_when_invalid_isbn(String isbn) {
            IsIsbnValidator validator = new IsIsbnValidator();
            TestModel model = new TestModel(isbn);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for ISBN: " + isbn);
        }

        @ParameterizedTest
        @MethodSource("validCreditCardFormats")
        void creditCard_should_pass_when_valid_card(String cardNumber) {
            CreditCardValidator validator = new CreditCardValidator();
            TestModel model = new TestModel(cardNumber);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for card: " + cardNumber);
        }

        @ParameterizedTest
        @MethodSource("invalidCreditCardFormats")
        void creditCard_should_fail_when_invalid_card(String cardNumber) {
            CreditCardValidator validator = new CreditCardValidator();
            TestModel model = new TestModel(cardNumber);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for card: " + cardNumber);
        }
    }

    // ========== PHONE & NETWORK TESTS ==========

    @Nested
    class PhoneAndNetworkTests {

        static Stream<String> validPhoneFormats() {
            return Stream.of(
                    "+1-555-123-4567",
                    "+1 (555) 123-4567",
                    "+1.555.123.4567",
                    "+1 555 123 4567",
                    "+905551234567",
                    "+90-555-123-4567",
                    "+90 555 123 4567",
                    "0555 123 4567",
                    "(0555) 1234567",
                    "+44 20 7946 0958",
                    "+49-30-901820",
                    "+33 1 23 45 67 89",
                    "+1-555-123-4567 ext 123",
                    "+1-555-123-4567 x123",
                    "555-1234",
                    "1-800-123-4567"
            );
        }

        static Stream<String> invalidPhoneFormats() {
            return Stream.of(
                    "abc",
                    "123",
                    "555-123",
                    "555-123-4567-890-123",
                    "+-()-",
                    "555 123 4567 890 123 456",
                    "+1-555-ABC-1234",
                    "++1-555-123-4567"
            );
        }

        static Stream<String> validIpv4Formats() {
            return Stream.of(
                    "192.168.1.1",
                    "10.0.0.1",
                    "172.16.0.1",
                    "8.8.8.8",
                    "255.255.255.255",
                    "0.0.0.0",
                    "127.0.0.1",
                    "198.51.100.1"
            );
        }

        static Stream<String> invalidIpv4Formats() {
            return Stream.of(
                    "999.999.999.999",
                    "192.168.1",
                    "192.168.1.1.1",
                    "192.168.1.256",
                    "192.168.01.1",
                    "192.168.1.",
                    ".192.168.1.1",
                    "192.168.1.a"
            );
        }

        static Stream<String> validIpv6Formats() {
            return Stream.of(
                    "2001:0db8:85a3:0000:0000:8a2e:0370:7334",
                    "2001:db8:85a3:0:0:8a2e:370:7334",
                    "2001:db8:85a3::8a2e:370:7334",
                    "::1",
                    "::",
                    "2001:0db8::0001",
                    "fe80::",
                    "2001:db8:3333:4444:5555:6666:7777:8888"
            );
        }

        static Stream<String> invalidIpv6Formats() {
            return Stream.of(
                    "not-ipv6",
                    "2001:0db8:85a3:0000:0000:8a2e:0370:7334:9999",
                    "2001::85a3::8a2e",
                    "2001:0gb8:85a3:0000:0000:8a2e:0370:7334",
                    "2001:db8:85a3:0:0:8a2e:370:7334:123",
                    "2001:db8:85a3"
            );
        }

        static Stream<String> validMacAddressFormats() {
            return Stream.of(
                    "00:1B:44:11:3A:B7",
                    "00-1B-44-11-3A-B7",
                    "001B.4411.3AB7",
                    "00:1b:44:11:3a:b7",
                    "00-1b-44-11-3a-b7",
                    "001B44113AB7"
            );
        }

        static Stream<String> invalidMacAddressFormats() {
            return Stream.of(
                    "not-mac",
                    "00:1B:44:11:3A",
                    "00:1B:44:11:3A:B7:89",
                    "00:1B:44:11:3A:BG",
                    "00-1B-44-11-3A-B7-89",
                    "00:1B:44:11:3A"
            );
        }

        @ParameterizedTest
        @MethodSource("validPhoneFormats")
        void isPhoneNumber_should_pass_when_valid_phone(String phoneNumber) {
            IsPhoneNumberValidator validator = new IsPhoneNumberValidator();
            TestModel model = new TestModel(phoneNumber);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for phone: " + phoneNumber);
        }

        @ParameterizedTest
        @MethodSource("invalidPhoneFormats")
        void isPhoneNumber_should_fail_when_invalid_phone(String phoneNumber) {
            IsPhoneNumberValidator validator = new IsPhoneNumberValidator();
            TestModel model = new TestModel(phoneNumber);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for phone: " + phoneNumber);
        }

        @ParameterizedTest
        @MethodSource("validIpv4Formats")
        void isIpv4_should_pass_when_valid_ipv4(String ip) {
            IsIpv4Validator validator = new IsIpv4Validator();
            TestModel model = new TestModel(ip);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for IPv4: " + ip);
        }

        @ParameterizedTest
        @MethodSource("invalidIpv4Formats")
        void isIpv4_should_fail_when_invalid_ipv4(String ip) {
            IsIpv4Validator validator = new IsIpv4Validator();
            TestModel model = new TestModel(ip);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for IPv4: " + ip);
        }

        @ParameterizedTest
        @MethodSource("validIpv6Formats")
        void isIpv6_should_pass_when_valid_ipv6(String ip) {
            IsIpv6Validator validator = new IsIpv6Validator();
            TestModel model = new TestModel(ip);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for IPv6: " + ip);
        }

        @ParameterizedTest
        @MethodSource("invalidIpv6Formats")
        void isIpv6_should_fail_when_invalid_ipv6(String ip) {
            IsIpv6Validator validator = new IsIpv6Validator();
            TestModel model = new TestModel(ip);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for IPv6: " + ip);
        }

        @ParameterizedTest
        @MethodSource("validMacAddressFormats")
        void isMacAddress_should_pass_when_valid_mac(String mac) {
            IsMacAddressValidator validator = new IsMacAddressValidator();
            TestModel model = new TestModel(mac);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for MAC: " + mac);
        }

        @ParameterizedTest
        @MethodSource("invalidMacAddressFormats")
        void isMacAddress_should_fail_when_invalid_mac(String mac) {
            IsMacAddressValidator validator = new IsMacAddressValidator();
            TestModel model = new TestModel(mac);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for MAC: " + mac);
        }
    }

    // ========== DATE & TIME TESTS ==========

    @Nested
    class DateTimeTests {

        @Test
        void isInPast_should_pass_when_date_is_in_past() {
            IsInPastValidator validator = new IsInPastValidator();
            TestModel model = new TestModel("test");
            model.setBirthDate(LocalDate.of(2000, 1, 1));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isInFuture_should_pass_when_date_is_in_future() {
            IsInFutureValidator validator = new IsInFutureValidator();
            TestModel model = new TestModel("test");
            model.setBirthDate(LocalDate.now().plusDays(10));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isToday_should_pass_when_date_is_today() {
            IsTodayValidator validator = new IsTodayValidator();
            TestModel model = new TestModel("test");
            model.setBirthDate(LocalDate.now());
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void minAge_should_pass_when_age_is_sufficient() {
            MinAgeValidator validator = new MinAgeValidator(18);
            TestModel model = new TestModel("test");
            model.setBirthDate(LocalDate.now().minusYears(20));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void minAge_should_fail_when_age_is_insufficient() {
            MinAgeValidator validator = new MinAgeValidator(18);
            TestModel model = new TestModel("test");
            model.setBirthDate(LocalDate.now().minusYears(10));
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void maxAge_should_pass_when_age_is_within_limit() {
            MaxAgeValidator validator = new MaxAgeValidator(100);
            TestModel model = new TestModel("test");
            model.setBirthDate(LocalDate.now().minusYears(50));
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }
    }

    // ========== COORDINATE TESTS ==========

    @Nested
    class CoordinateTests {

        @Test
        void isLatitude_should_pass_when_valid_latitude() {
            IsLatitudeValidator validator = new IsLatitudeValidator();
            TestModel model = new TestModel("test");
            model.setLatitude(45.5);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isLatitude_should_fail_when_out_of_range() {
            IsLatitudeValidator validator = new IsLatitudeValidator();
            TestModel model = new TestModel("test");
            model.setLatitude(100.0);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void isLongitude_should_pass_when_valid_longitude() {
            IsLongitudeValidator validator = new IsLongitudeValidator();
            TestModel model = new TestModel("test");
            model.setLongitude(120.0);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isLongitude_should_fail_when_out_of_range() {
            IsLongitudeValidator validator = new IsLongitudeValidator();
            TestModel model = new TestModel("test");
            model.setLongitude(200.0);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }
    }

    // ========== PASSWORD TESTS ==========

    @Nested
    class PasswordTests {

        static Stream<String> validStrongPasswords() {
            return Stream.of(
                    "StrongP@ss123",
                    "Password@1234",
                    "MyP@ssw0rd!",
                    "Test@123456",
                    "A1@bcdefg"
            );
        }

        static Stream<String> invalidStrongPasswords() {
            return Stream.of(
                    "weak",
                    "nouppercase123@",
                    "NOLOWERCASE123@",
                    "NoDigit@Password",
                    "NoSpecial123Password",
                    "Sh0rt!",
                    "VeryLongPasswordWithoutSpecial1234567890"
            );
        }

        @Test
        void containsUppercase_should_pass_when_has_uppercase() {
            ContainsUppercaseValidator validator = new ContainsUppercaseValidator();
            TestModel model = new TestModel("Password");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void containsLowercase_should_pass_when_has_lowercase() {
            ContainsLowercaseValidator validator = new ContainsLowercaseValidator();
            TestModel model = new TestModel("Password");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void containsDigit_should_pass_when_has_digit() {
            ContainsDigitValidator validator = new ContainsDigitValidator();
            TestModel model = new TestModel("Password123");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void containsSpecialChar_should_pass_when_has_special_char() {
            ContainsSpecialCharValidator validator = new ContainsSpecialCharValidator();
            TestModel model = new TestModel("Password@123");
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @ParameterizedTest
        @MethodSource("validStrongPasswords")
        void strongPassword_should_pass_when_meets_all_criteria(String password) {
            StrongPasswordValidator validator = new StrongPasswordValidator(8, 20);
            TestModel model = new TestModel(password);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for password: " + password);
        }

        @ParameterizedTest
        @MethodSource("invalidStrongPasswords")
        void strongPassword_should_fail_when_missing_criteria(String password) {
            StrongPasswordValidator validator = new StrongPasswordValidator(8, 20);
            TestModel model = new TestModel(password);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for password: " + password);
        }
    }

    // ========== ADDITIONAL TESTS ==========

    @Nested
    class AdditionalTests {

        @Test
        void isPort_should_pass_when_valid_port() {
            IsPortValidator validator = new IsPortValidator();
            TestModel model = new TestModel("test");
            model.setPort(8080);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isPort_should_fail_when_invalid_port() {
            IsPortValidator validator = new IsPortValidator();
            TestModel model = new TestModel("test");
            model.setPort(70000);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid());
        }

        @Test
        void isPercentage_should_pass_when_valid_percentage() {
            IsPercentageValidator validator = new IsPercentageValidator();
            TestModel model = new TestModel("test");
            model.setPercentage(75.5);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isDivisibleBy_should_pass_when_divisible() {
            IsDivisibleByValidator validator = new IsDivisibleByValidator(5);
            TestModel model = new TestModel("test");
            model.setAge(25);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isEven_should_pass_when_even_number() {
            IsEvenValidator validator = new IsEvenValidator();
            TestModel model = new TestModel("test");
            model.setAge(24);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isOdd_should_pass_when_odd_number() {
            IsOddValidator validator = new IsOddValidator();
            TestModel model = new TestModel("test");
            model.setAge(25);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isTrue_should_pass_when_true() {
            IsTrueValidator validator = new IsTrueValidator();
            TestModel model = new TestModel("test");
            model.setActive(true);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }

        @Test
        void isFalse_should_pass_when_false() {
            IsFalseValidator validator = new IsFalseValidator();
            TestModel model = new TestModel("test");
            model.setActive(false);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid());
        }
    }

    // ========== CASE VALIDATION TESTS ==========

    @Nested
    class CaseValidationTests {

        static Stream<String> validCamelCaseFormats() {
            return Stream.of(
                    "camelCase",
                    "camelCaseValue",
                    "camel123Case",
                    "camel",
                    "camelCase123"
            );
        }

        static Stream<String> invalidCamelCaseFormats() {
            return Stream.of(
                    "PascalCase",
                    "snake_case",
                    "kebab-case",
                    "Camel Case",
                    "123camel",
                    "_camelCase"
            );
        }

        static Stream<String> validPascalCaseFormats() {
            return Stream.of(
                    "PascalCase",
                    "PascalCaseValue",
                    "Pascal123Case",
                    "Pascal",
                    "PascalCase123"
            );
        }

        static Stream<String> invalidPascalCaseFormats() {
            return Stream.of(
                    "camelCase",
                    "snake_case",
                    "kebab-case",
                    "Pascal Case",
                    "123Pascal",
                    "_PascalCase"
            );
        }

        static Stream<String> validSnakeCaseFormats() {
            return Stream.of(
                    "snake_case",
                    "snake_case_value",
                    "snake_123_case",
                    "snake",
                    "SNAKE_CASE",
                    "snake_case_123"
            );
        }

        static Stream<String> invalidSnakeCaseFormats() {
            return Stream.of(
                    "camelCase",
                    "kebab-case",
                    "snake case",
                    "_snake_case",
                    "snake_case_",
                    "snake__case"
            );
        }

        static Stream<String> validKebabCaseFormats() {
            return Stream.of(
                    "kebab-case",
                    "kebab-case-value",
                    "kebab-123-case",
                    "kebab",
                    "kebab-case-123"
            );
        }

        static Stream<String> invalidKebabCaseFormats() {
            return Stream.of(
                    "camelCase",
                    "snake_case",
                    "kebab case",
                    "-kebab-case",
                    "kebab-case-",
                    "kebab--case"
            );
        }

        @ParameterizedTest
        @MethodSource("validCamelCaseFormats")
        void isCamelCase_should_pass_when_camel_case(String value) {
            IsCamelCaseValidator validator = new IsCamelCaseValidator();
            TestModel model = new TestModel(value);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for: " + value);
        }

        @ParameterizedTest
        @MethodSource("invalidCamelCaseFormats")
        void isCamelCase_should_fail_when_not_camel_case(String value) {
            IsCamelCaseValidator validator = new IsCamelCaseValidator();
            TestModel model = new TestModel(value);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for: " + value);
        }

        @ParameterizedTest
        @MethodSource("validPascalCaseFormats")
        void isPascalCase_should_pass_when_pascal_case(String value) {
            IsPascalCaseValidator validator = new IsPascalCaseValidator();
            TestModel model = new TestModel(value);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for: " + value);
        }

        @ParameterizedTest
        @MethodSource("invalidPascalCaseFormats")
        void isPascalCase_should_fail_when_not_pascal_case(String value) {
            IsPascalCaseValidator validator = new IsPascalCaseValidator();
            TestModel model = new TestModel(value);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for: " + value);
        }

        @ParameterizedTest
        @MethodSource("validSnakeCaseFormats")
        void isSnakeCase_should_pass_when_snake_case(String value) {
            IsSnakeCaseValidator validator = new IsSnakeCaseValidator();
            TestModel model = new TestModel(value);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for: " + value);
        }

        @ParameterizedTest
        @MethodSource("invalidSnakeCaseFormats")
        void isSnakeCase_should_fail_when_not_snake_case(String value) {
            IsSnakeCaseValidator validator = new IsSnakeCaseValidator();
            TestModel model = new TestModel(value);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for: " + value);
        }

        @ParameterizedTest
        @MethodSource("validKebabCaseFormats")
        void isKebabCase_should_pass_when_kebab_case(String value) {
            IsKebabCaseValidator validator = new IsKebabCaseValidator();
            TestModel model = new TestModel(value);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Failed for: " + value);
        }

        @ParameterizedTest
        @MethodSource("invalidKebabCaseFormats")
        void isKebabCase_should_fail_when_not_kebab_case(String value) {
            IsKebabCaseValidator validator = new IsKebabCaseValidator();
            TestModel model = new TestModel(value);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should fail for: " + value);
        }
    }

    // ========== SECURITY TESTS ==========

    @Nested
    class SecurityTests {

        static Stream<String> safeInputs() {
            return Stream.of(
                    "safe input",
                    "normal text",
                    "user@example.com",
                    "https://example.com",
                    "Hello World 123"
            );
        }

        static Stream<String> sqlInjectionInputs() {
            return Stream.of(
                    "' OR 1=1 --",
                    "\" OR \"1\"=\"1",
                    "'; DROP TABLE users; --",
                    "1' OR '1' = '1",
                    "admin' --",
                    "SELECT * FROM users",
                    "UNION SELECT null, null",
                    "/* comment */"
            );
        }

        static Stream<String> xssInputs() {
            return Stream.of(
                    "<script>alert('xss')</script>",
                    "<img src=x onerror=alert(1)>",
                    "javascript:alert('xss')",
                    "onload=alert('xss')",
                    "<iframe src=javascript:alert('xss')>",
                    "expression(alert('xss'))",
                    "<svg onload=alert('xss')>"
            );
        }

        @ParameterizedTest
        @MethodSource("safeInputs")
        void containsNoSqlInjection_should_pass_when_safe(String input) {
            ContainsNoSqlInjectionValidator validator = new ContainsNoSqlInjectionValidator();
            TestModel model = new TestModel(input);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Should be safe: " + input);
        }

        @ParameterizedTest
        @MethodSource("sqlInjectionInputs")
        void containsNoSqlInjection_should_fail_when_contains_sql(String input) {
            ContainsNoSqlInjectionValidator validator = new ContainsNoSqlInjectionValidator();
            TestModel model = new TestModel(input);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should detect SQL: " + input);
        }

        @ParameterizedTest
        @MethodSource("safeInputs")
        void containsNoXss_should_pass_when_safe(String input) {
            ContainsNoXssValidator validator = new ContainsNoXssValidator();
            TestModel model = new TestModel(input);
            ValidationResult result = validator.validate(model);
            assertTrue(result.isValid(), "Should be safe: " + input);
        }

        @ParameterizedTest
        @MethodSource("xssInputs")
        void containsNoXss_should_fail_when_contains_script(String input) {
            ContainsNoXssValidator validator = new ContainsNoXssValidator();
            TestModel model = new TestModel(input);
            ValidationResult result = validator.validate(model);
            assertFalse(result.isValid(), "Should detect XSS: " + input);
        }
    }

    // ========== WITH MESSAGE TESTS ==========

    @Nested
    class WithMessageTests {

        @Test
        void withMessage_should_override_default_message() {
            WithMessageValidator validator = new WithMessageValidator();
            TestModel model = new TestModel(null);
            ValidationResult result = validator.validate(model);

            assertFalse(result.isValid());

            boolean hasCustomMessage = result.getErrors().stream()
                    .anyMatch(error ->
                            error.property().equals("value") &&
                                    error.message().contains("Custom error message"));
            assertTrue(hasCustomMessage, "Should have custom error message");
        }
    }
}