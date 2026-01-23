package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class NotEqualToValidator extends Validator<TestModel> {
    public NotEqualToValidator(String value) {
        ruleFor(TestModel::getValue).notEqualTo(value);
    }
}
