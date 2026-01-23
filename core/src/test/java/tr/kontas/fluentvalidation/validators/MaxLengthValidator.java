package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class MaxLengthValidator extends Validator<TestModel> {
    public MaxLengthValidator(int max) {
        ruleFor(TestModel::getValue).maxLength(max);
    }
}
