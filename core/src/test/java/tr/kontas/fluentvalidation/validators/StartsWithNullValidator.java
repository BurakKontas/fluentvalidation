package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class StartsWithNullValidator extends Validator<TestModel> {
    public StartsWithNullValidator() {
        ruleFor(TestModel::getValue).startsWith("test");
    }
}
