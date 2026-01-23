package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class MinLengthValidator extends Validator<TestModel> {
    public MinLengthValidator(int min) {
        ruleFor(TestModel::getValue).minLength(min);
    }
}
