package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsPositiveWithZeroValidator extends Validator<TestModel> {
    public IsPositiveWithZeroValidator() {
        ruleFor(TestModel::getAge).isPositive();
    }
}
