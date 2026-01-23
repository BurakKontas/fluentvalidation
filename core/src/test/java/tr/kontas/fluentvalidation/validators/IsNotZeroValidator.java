package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsNotZeroValidator extends Validator<TestModel> {
    public IsNotZeroValidator() {
        ruleFor(TestModel::getAge).isNotZero();
    }
}
