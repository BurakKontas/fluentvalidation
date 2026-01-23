package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class StrongPasswordValidator extends Validator<TestModel> {
    public StrongPasswordValidator(int min, int max) {
        ruleFor(TestModel::getValue).strongPassword(min, max);
    }
}
