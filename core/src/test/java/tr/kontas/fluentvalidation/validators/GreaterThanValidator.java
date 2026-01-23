package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

// Numeric Rules
public class GreaterThanValidator extends Validator<TestModel> {
    public GreaterThanValidator(int value) {
        ruleFor(TestModel::getAge).greaterThan(value);
    }
}
