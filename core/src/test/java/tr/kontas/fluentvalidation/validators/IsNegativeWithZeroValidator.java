package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsNegativeWithZeroValidator extends Validator<TestModel> {
    public IsNegativeWithZeroValidator() {
        ruleFor(TestModel::getAge).isNegative();
    }
}
