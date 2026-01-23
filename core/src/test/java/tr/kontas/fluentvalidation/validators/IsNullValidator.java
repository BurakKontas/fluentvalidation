package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsNullValidator extends Validator<TestModel> {
    public IsNullValidator() {
        ruleFor(TestModel::getValue).isNull();
    }
}
