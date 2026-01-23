package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class LengthNullValidator extends Validator<TestModel> {
    public LengthNullValidator() {
        ruleFor(TestModel::getValue).length(5);
    }
}
