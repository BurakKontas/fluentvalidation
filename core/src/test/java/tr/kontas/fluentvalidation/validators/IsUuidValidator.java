package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsUuidValidator extends Validator<TestModel> {
    public IsUuidValidator() {
        ruleFor(TestModel::getValue).isUuid();
    }
}
