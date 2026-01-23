package tr.kontas.fluentvalidation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tr.kontas.fluentvalidation.validation.ValidationResult;
import tr.kontas.fluentvalidation.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;

public class ConditionalRuleTest {

    // ========== BASIC WHEN TESTS ==========
    @Nested
    class BasicWhenTests {
        static class USAgeValidator extends Validator<User> {
            public USAgeValidator() {
                ruleFor(User::getAge)
                        .when(u -> "US".equals(u.getCountry()))
                        .greaterThanOrEqualTo(21);
            }
        }

        @Test
        void when_should_apply_rule_when_condition_is_true() {
            USAgeValidator validator = new USAgeValidator();
            User user = new User();
            user.setCountry("US");
            user.setAge(20);

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream().anyMatch(e -> e.property().equals("age")));
        }

        @Test
        void when_should_skip_rule_when_condition_is_false() {
            USAgeValidator validator = new USAgeValidator();
            User user = new User();
            user.setCountry("TR");
            user.setAge(20);

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }

        @Test
        void when_should_pass_when_condition_true_and_value_valid() {
            USAgeValidator validator = new USAgeValidator();
            User user = new User();
            user.setCountry("US");
            user.setAge(25);

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }
    }

    // ========== BASIC UNLESS TESTS ==========
    @Nested
    class BasicUnlessTests {
        static class MiddleNameValidator extends Validator<User> {
            public MiddleNameValidator() {
                ruleFor(User::getMiddleName)
                        .unless(User::hasMiddleName)
                        .isNull();
            }
        }

        @Test
        void unless_should_apply_rule_when_condition_is_false() {
            MiddleNameValidator validator = new MiddleNameValidator();
            User user = new User();
            user.setHasMiddleName(false);
            user.setMiddleName("John");

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
        }

        @Test
        void unless_should_skip_rule_when_condition_is_true() {
            MiddleNameValidator validator = new MiddleNameValidator();
            User user = new User();
            user.setHasMiddleName(true);
            user.setMiddleName("John");

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }

        @Test
        void unless_should_pass_when_condition_false_and_value_valid() {
            MiddleNameValidator validator = new MiddleNameValidator();
            User user = new User();
            user.setHasMiddleName(false);
            user.setMiddleName(null);

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }
    }

    // ========== MULTIPLE WHEN TESTS ==========
    @Nested
    class MultipleWhenTests {
        static class EmailValidator extends Validator<User> {
            public EmailValidator() {
                ruleFor(User::getEmail)
                        .when(User::isPremium)
                        .notNull()
                        .email()
                        .maxLength(100);

                ruleFor(User::getEmail)
                        .when(u -> !u.isPremium())
                        .notNull();
            }
        }

        @Test
        void multiple_when_should_apply_first_condition_rules() {
            EmailValidator validator = new EmailValidator();
            User user = new User();
            user.setPremium(true);
            user.setEmail("invalid-email");

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream().anyMatch(e -> e.property().equals("email")));
        }

        @Test
        void multiple_when_should_apply_second_condition_rules() {
            EmailValidator validator = new EmailValidator();
            User user = new User();
            user.setPremium(false);
            user.setEmail(null);

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
        }

        @Test
        void multiple_when_should_pass_when_premium_with_valid_email() {
            EmailValidator validator = new EmailValidator();
            User user = new User();
            user.setPremium(true);
            user.setEmail("user@example.com");

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }

        @Test
        void multiple_when_should_pass_when_non_premium_with_non_null_email() {
            EmailValidator validator = new EmailValidator();
            User user = new User();
            user.setPremium(false);
            user.setEmail("anyemail");

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }
    }

    // ========== WHEN + UNLESS COMBINATION TESTS ==========
    @Nested
    class WhenUnlessCombinationTests {
        static class PhoneValidator extends Validator<User> {
            public PhoneValidator() {
                ruleFor(User::getPhone)
                        .when(u -> "TR".equals(u.getCountry()))
                        .matches("^\\+90\\d{10}$");

                ruleFor(User::getPhone)
                        .unless(User::isVerified)
                        .notNull();
            }
        }

        @Test
        void when_and_unless_should_both_apply() {
            PhoneValidator validator = new PhoneValidator();
            User user = new User();
            user.setCountry("TR");
            user.setVerified(false);
            user.setPhone(null);

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
        }

        @Test
        void when_should_apply_unless_should_skip() {
            PhoneValidator validator = new PhoneValidator();
            User user = new User();
            user.setCountry("TR");
            user.setVerified(true);
            user.setPhone("invalid");

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream().anyMatch(e -> e.message().contains("pattern")));
        }

        @Test
        void when_should_skip_unless_should_apply() {
            PhoneValidator validator = new PhoneValidator();
            User user = new User();
            user.setCountry("US");
            user.setVerified(false);
            user.setPhone(null);

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
        }

        @Test
        void both_when_and_unless_should_skip() {
            PhoneValidator validator = new PhoneValidator();
            User user = new User();
            user.setCountry("US");
            user.setVerified(true);
            user.setPhone(null);

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }
    }

    // ========== NORMAL + CONDITIONAL MIX TESTS ==========
    @Nested
    class NormalAndConditionalMixTests {
        static class UsernameValidator extends Validator<User> {
            public UsernameValidator() {
                ruleFor(User::getUsername)
                        .notNull()
                        .minLength(3)
                        .maxLength(50)
                        .when(User::isPublic)
                        .matches("^[a-zA-Z0-9_]+$");
            }
        }

        @Test
        void should_apply_normal_rules_always() {
            UsernameValidator validator = new UsernameValidator();
            User user = new User();
            user.setPublic(false);
            user.setUsername("ab");

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream().anyMatch(e -> e.message().contains("at least 3")));
        }

        @Test
        void should_apply_conditional_rule_when_public() {
            UsernameValidator validator = new UsernameValidator();
            User user = new User();
            user.setPublic(true);
            user.setUsername("user@123");

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream().anyMatch(e -> e.message().contains("pattern")));
        }

        @Test
        void should_skip_conditional_rule_when_not_public() {
            UsernameValidator validator = new UsernameValidator();
            User user = new User();
            user.setPublic(false);
            user.setUsername("user@123");

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }

        @Test
        void should_pass_when_all_rules_satisfied() {
            UsernameValidator validator = new UsernameValidator();
            User user = new User();
            user.setPublic(true);
            user.setUsername("valid_user_123");

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }
    }

    // ========== NULL SAFETY TESTS ==========
    @Nested
    class NullSafetyTests {
        static class NullSafeValidator extends Validator<User> {
            public NullSafeValidator() {
                ruleFor(User::getAge)
                        .when(u -> u.getCountry() != null && u.getCountry().equals("US"))
                        .greaterThanOrEqualTo(21);
            }
        }

        @Test
        void should_handle_null_country_gracefully() {
            NullSafeValidator validator = new NullSafeValidator();
            User user = new User();
            user.setCountry(null);
            user.setAge(18);

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }

        @Test
        void should_apply_rule_when_country_not_null() {
            NullSafeValidator validator = new NullSafeValidator();
            User user = new User();
            user.setCountry("US");
            user.setAge(18);

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
        }
    }

    // ========== CASCADING CONDITIONALS ==========
    @Nested
    class CascadingConditionalsTests {
        static class OrderValidator extends Validator<Order> {
            public OrderValidator() {
                ruleFor(Order::getShippingSpeed)
                        .when(Order::isExpressShipping)
                        .notNull();

                ruleFor(Order::getShippingSpeed)
                        .when(o -> o.isExpressShipping() && !o.isPremiumMember())
                        .isInEnum(ShippingSpeed.class);

                ruleFor(Order::getShippingSpeed)
                        .when(o -> o.isExpressShipping() && !o.isPremiumMember() && o.getTotalAmount() < 100)
                        .equalTo(ShippingSpeed.STANDARD);
            }
        }

        @Test
        void should_allow_express_shipping_for_premium_members() {
            OrderValidator validator = new OrderValidator();
            Order order = new Order();
            order.setExpressShipping(true);
            order.setPremiumMember(true);
            order.setTotalAmount(50);
            order.setShippingSpeed(ShippingSpeed.EXPRESS);

            ValidationResult result = validator.validate(order);

            assertTrue(result.isValid());
        }
    }

    // ========== ADVANCED CONDITIONAL TESTS ==========
// ========== ADVANCED CONDITIONAL TESTS FOR CURRENT USER CLASS ==========
    @Nested
    class AdvancedConditionalTests {

        static class AdvancedUserValidator extends Validator<User> {
            public AdvancedUserValidator() {
                // 1. Premium user email validation if unverified
                ruleFor(User::getEmail)
                        .when(u -> u.isPremium() && u.getAge() != null && u.getAge() > 18)
                        .unless(User::isVerified)
                        .email()
                        .maxLength(100);

                // 2. Username pattern only if public
                ruleFor(User::getUsername)
                        .when(User::isPublic)
                        .must(u -> u != null && u.startsWith("user_"), "should start with user_");

                // 3. Middle name should be null unless hasMiddleName is true
                ruleFor(User::getMiddleName)
                        .unless(User::hasMiddleName)
                        .isNull();

                // 4. Phone number for unverified users must start with +
                ruleFor(User::getPhone)
                        .unless(User::isVerified)
                        .matches("^\\+\\d{10,15}$");
            }
        }

        @Test
        void email_should_fail_when_premium_unverified_and_invalid() {
            AdvancedUserValidator validator = new AdvancedUserValidator();
            User user = new User();
            user.setPremium(true);
            user.setVerified(false);
            user.setAge(25);
            user.setEmail("invalid-email");

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream().anyMatch(e -> e.property().equals("email")));
        }

        @Test
        void username_should_fail_dynamic_validation() {
            AdvancedUserValidator validator = new AdvancedUserValidator();
            User user = new User();
            user.setPublic(true);
            user.setUsername("admin123");

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream().anyMatch(e -> e.property().equals("username")));
        }

        @Test
        void middleName_should_fail_when_should_be_null() {
            AdvancedUserValidator validator = new AdvancedUserValidator();
            User user = new User();
            user.setHasMiddleName(false);
            user.setMiddleName("John");

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream().anyMatch(e -> e.property().equals("middleName")));
        }

        @Test
        void phone_should_fail_if_unverified_and_invalid_format() {
            AdvancedUserValidator validator = new AdvancedUserValidator();
            User user = new User();
            user.setVerified(false);
            user.setPhone("5551234567"); // + olmadan

            ValidationResult result = validator.validate(user);

            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream().anyMatch(e -> e.property().equals("phone")));
        }

        @Test
        void all_rules_should_pass_when_valid() {
            AdvancedUserValidator validator = new AdvancedUserValidator();
            User user = new User();
            user.setPremium(true);
            user.setVerified(true);
            user.setAge(30);
            user.setEmail("valid@example.com");
            user.setPublic(true);
            user.setUsername("user_john");
            user.setHasMiddleName(true);
            user.setMiddleName("Doe");
            user.setPhone("+12345678901");

            ValidationResult result = validator.validate(user);

            assertTrue(result.isValid());
        }
    }
}
