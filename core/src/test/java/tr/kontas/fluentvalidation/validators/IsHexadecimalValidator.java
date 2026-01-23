package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsHexadecimalValidator extends Validator<TestModel> {
    public IsHexadecimalValidator() {
        ruleFor(TestModel::getValue).isHexadecimal();
    }
}
