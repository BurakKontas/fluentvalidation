package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsNegativeValidator extends Validator<TestModel> {
    public IsNegativeValidator() {
        ruleFor(TestModel::getAge).isNegative();
    }
}
