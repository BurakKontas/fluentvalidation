package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class NotEmptyMapValidator extends Validator<TestModel> {
    public NotEmptyMapValidator() {
        ruleFor(TestModel::getMap).notEmpty();
    }
}
