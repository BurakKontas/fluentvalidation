package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class NotEmptyStringValidator extends Validator<TestModel> {
    public NotEmptyStringValidator() {
        ruleFor(TestModel::getValue).notEmpty();
    }
}
