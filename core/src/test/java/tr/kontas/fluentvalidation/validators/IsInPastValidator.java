package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsInPastValidator extends Validator<TestModel> {
    public IsInPastValidator() {
        ruleFor(TestModel::getBirthDate).isInPast();
    }
}
