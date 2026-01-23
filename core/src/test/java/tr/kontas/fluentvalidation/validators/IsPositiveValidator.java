package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsPositiveValidator extends Validator<TestModel> {
    public IsPositiveValidator() {
        ruleFor(TestModel::getAge).isPositive();
    }
}
