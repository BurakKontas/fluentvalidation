package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class NotEmptyCollectionValidator extends Validator<TestModel> {
    public NotEmptyCollectionValidator() {
        ruleFor(TestModel::getList).notEmpty();
    }
}
