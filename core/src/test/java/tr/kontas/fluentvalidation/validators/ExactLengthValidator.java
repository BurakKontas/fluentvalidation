package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

// String Length
public class ExactLengthValidator extends Validator<TestModel> {
    public ExactLengthValidator(int length) {
        ruleFor(TestModel::getValue).length(length);
    }
}
