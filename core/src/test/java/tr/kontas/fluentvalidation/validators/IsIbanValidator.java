package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsIbanValidator extends Validator<TestModel> {
    public IsIbanValidator() {
        ruleFor(TestModel::getValue).isIban();
    }
}
