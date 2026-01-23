package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsFalseValidator extends Validator<TestModel> {
    public IsFalseValidator() {
        ruleFor(TestModel::isActive).isFalse();
    }
}
