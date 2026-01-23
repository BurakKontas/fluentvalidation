package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class TestValidator extends Validator<TestModel> {
    public TestValidator() {
        ruleFor(TestModel::getValue)
                .notBlank();
    }
}
