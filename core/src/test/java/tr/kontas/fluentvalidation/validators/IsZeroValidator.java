package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsZeroValidator extends Validator<TestModel> {
    public IsZeroValidator() {
        ruleFor(TestModel::getAge).isZero();
    }
}
