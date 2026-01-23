package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsTrueValidator extends Validator<TestModel> {
    public IsTrueValidator() {
        ruleFor(TestModel::isActive).isTrue();
    }
}
