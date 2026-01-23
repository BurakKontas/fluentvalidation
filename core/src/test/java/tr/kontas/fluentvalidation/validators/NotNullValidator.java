package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class NotNullValidator extends Validator<TestModel> {
    public NotNullValidator() {
        ruleFor(TestModel::getValue).notNull();
    }
}
