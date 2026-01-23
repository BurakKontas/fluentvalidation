package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class LessThanOrEqualValidator extends Validator<TestModel> {
    public LessThanOrEqualValidator(int value) {
        ruleFor(TestModel::getAge).lessThanOrEqualTo(value);
    }
}
