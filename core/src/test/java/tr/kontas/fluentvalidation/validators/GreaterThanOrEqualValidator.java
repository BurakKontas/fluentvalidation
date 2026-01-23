package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class GreaterThanOrEqualValidator extends Validator<TestModel> {
    public GreaterThanOrEqualValidator(int value) {
        ruleFor(TestModel::getAge).greaterThanOrEqualTo(value);
    }
}
