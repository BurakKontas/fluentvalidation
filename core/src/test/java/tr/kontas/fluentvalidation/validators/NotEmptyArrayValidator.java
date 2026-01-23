package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class NotEmptyArrayValidator extends Validator<TestModel> {
    public NotEmptyArrayValidator() {
        ruleFor(TestModel::getStringArray).notEmpty();
    }
}
