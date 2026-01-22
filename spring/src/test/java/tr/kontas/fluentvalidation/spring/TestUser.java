package tr.kontas.fluentvalidation.spring;

import tr.kontas.fluentvalidation.Validate;

@Validate(validator = TestUserValidator.class)
public class TestUser {

    private final String email;

    public TestUser(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}